package com.tehronshoh.tk_ozarakhsh.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tehronshoh.tk_ozarakhsh.adapter.GazalAdapter
import com.tehronshoh.tk_ozarakhsh.data.ParentType
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.data.PoemType
import com.tehronshoh.tk_ozarakhsh.databinding.FragmentGazalBinding
import com.tehronshoh.tk_ozarakhsh.viewmodels.ActivityViewModel

private const val POEM_TYPE = "param1"
private const val PARENT_TYPE = "param2"

class GazalFragment : Fragment() {
    private var _binding: FragmentGazalBinding? = null
    private val binding get() = _binding!!

    // Params
    private lateinit var poemType: PoemType
    private lateinit var parentType: ParentType

    private val viewModel: ActivityViewModel by activityViewModels()

    private lateinit var gazalAdapter: GazalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            poemType = PoemType.values()[it.getInt(POEM_TYPE)]
            parentType = ParentType.values()[it.getInt(PARENT_TYPE)]
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGazalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setting()
    }

    private fun startShimming() {
        binding.shimmerLayout.visibility = VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.gazalList.visibility = GONE
    }

    private fun stopShimming() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = GONE
        binding.gazalList.visibility = VISIBLE
    }

    private fun setting() {
        startShimming()
        gazalAdapter = GazalAdapter(parentType == ParentType.SEARCH)
        binding.gazalList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = gazalAdapter.also {
                it.onItemClickListener = { poem ->
                    viewModel.onPoemClick(poem)
                }
            }
        }

        when (parentType) {
            ParentType.SEARCH -> {
                viewModel.search("", poemType)
                if (poemType == PoemType.GAZAL) viewModel.searchGazalLiveData.observe(
                    viewLifecycleOwner
                ) {
                    submitAdapter(it)
                }
                else if (poemType == PoemType.SHER) viewModel.searchSherLiveData.observe(
                    viewLifecycleOwner
                ) {
                    submitAdapter(it)
                }
            }
            ParentType.ASHOR -> {
                viewModel.getListOfPoemsbyType(poemType).observe(viewLifecycleOwner) {
                    submitAdapter(it)
                }
            }
            ParentType.FAVORITE -> {
                viewModel.getListOfFavorite(poemType).observe(viewLifecycleOwner) {
                    submitAdapter(it)
                }
            }
        }
    }

    private fun submitAdapter(list: List<Poem>) {
        gazalAdapter.apply {
            searchingText = viewModel.searchingText
            submitList(list)
        }
        stopShimming()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(poemType: PoemType, parentType: ParentType) = GazalFragment().apply {
            arguments = Bundle().apply {
                putInt(POEM_TYPE, poemType.ordinal)
                putInt(PARENT_TYPE, parentType.ordinal)
            }
        }
    }
}
