package com.tehronshoh.tk_ozarakhsh.viewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.databinding.FragmentGazalSherItemBinding
import com.tehronshoh.tk_ozarakhsh.viewmodels.ActivityViewModel
import kotlin.math.abs

class GazalSherItemFragment : Fragment() {
    private var _binding: FragmentGazalSherItemBinding? = null
    private val binding get() = _binding!!

    private val args: GazalSherItemFragmentArgs by navArgs()
    private val viewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGazalSherItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingToolBar()
        args.poem.apply {
            binding.toolBar.title = title
            binding.poemText.text = text
        }
    }

    private fun settingToolBar() {
        setFavoriteIcon()
        binding.toolBar.apply {
            navigationIcon = ContextCompat.getDrawable(
                requireContext(), R.drawable.back_button
            )
            setNavigationIconTint(ContextCompat.getColor(requireContext(), R.color.white))
            setNavigationOnClickListener {
                (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController.navigateUp()
            }
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.favorite_button -> {
                        args.poem.isFavorite = abs(args.poem.isFavorite - 1)
                        viewModel.updatePoem(args.poem)
                        setFavoriteIcon()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setFavoriteIcon() {
        binding.toolBar.menu[0].setIcon(
            if (args.poem.isFavorite == 1) R.drawable.ic_baseline_favorite_24
            else R.drawable.ic_baseline_favorite_border_24
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}