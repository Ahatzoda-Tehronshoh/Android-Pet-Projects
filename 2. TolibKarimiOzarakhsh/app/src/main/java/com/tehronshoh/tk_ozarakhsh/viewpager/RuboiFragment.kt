package com.tehronshoh.tk_ozarakhsh.viewpager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tehronshoh.tk_ozarakhsh.ui.activities.BlurActivity
import com.tehronshoh.tk_ozarakhsh.utils.Vibrate
import com.tehronshoh.tk_ozarakhsh.adapter.RuboiAdapter
import com.tehronshoh.tk_ozarakhsh.data.ParentType
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.data.PoemType
import com.tehronshoh.tk_ozarakhsh.databinding.FragmentRuboiBinding
import com.tehronshoh.tk_ozarakhsh.ui.activities.drawingBitmap
import com.tehronshoh.tk_ozarakhsh.viewmodels.ActivityViewModel

private const val PARENT_TYPE = "param1"

class RuboiFragment : Fragment() {
    private var _binding: FragmentRuboiBinding? = null
    private val binding get() = _binding!!

    private lateinit var parentType: ParentType
    lateinit var ruboiAdapter: RuboiAdapter

    private lateinit var viewModel: ActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            parentType = ParentType.values()[it.getInt(PARENT_TYPE)]
        }
        viewModel = ViewModelProvider(requireActivity())[ActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRuboiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ruboiAdapter = RuboiAdapter(parentType == ParentType.SEARCH)

        binding.ruboiList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ruboiAdapter.also {
                it.onClickItem = { poem, bitmap ->
                    val vibrate = Vibrate(requireContext())
                    if (vibrate.canVibrate())
                        vibrate.vibrate(25)

                    val intent = Intent(requireActivity(), BlurActivity::class.java)
                    drawingBitmap = bitmap
                    intent.putExtra("poem", poem)
                    requireActivity().startActivity(intent)
                }
            }
        }

        when (parentType) {
            ParentType.SEARCH -> {
                viewModel.search("", PoemType.RUBOI)
                viewModel.searchRuboiLiveData.observe(viewLifecycleOwner) {
                    submitAdapter(it)
                }
            }
            ParentType.ASHOR -> {
                viewModel.getListOfPoemsbyType(PoemType.RUBOI).observe(viewLifecycleOwner) {
                    submitAdapter(it)
                }
            }
            ParentType.FAVORITE -> {
                viewModel.getListOfFavorite(PoemType.RUBOI).observe(viewLifecycleOwner) {
                    submitAdapter(it)
                }
            }
        }
    }

    private fun submitAdapter(list: List<Poem>) {
        ruboiAdapter.apply {
            searchingText = viewModel.searchingText
            submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(parentType: ParentType) = RuboiFragment().apply {
            arguments = Bundle().apply {
                putInt(PARENT_TYPE, parentType.ordinal)
            }
        }
    }

}