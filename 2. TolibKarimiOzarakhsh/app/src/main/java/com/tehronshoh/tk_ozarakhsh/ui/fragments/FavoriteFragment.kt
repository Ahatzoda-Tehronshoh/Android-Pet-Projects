package com.tehronshoh.tk_ozarakhsh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayoutMediator
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.data.ParentType
import com.tehronshoh.tk_ozarakhsh.data.PoemType
import com.tehronshoh.tk_ozarakhsh.databinding.FragmentFavoriteBinding
import com.tehronshoh.tk_ozarakhsh.viewmodels.ActivityViewModel
import com.tehronshoh.tk_ozarakhsh.viewpager.GazalFragment
import com.tehronshoh.tk_ozarakhsh.viewpager.RuboiFragment
import com.tehronshoh.tk_ozarakhsh.viewpager.ViewPagerAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewPager()
    }

    private fun settingViewPager() {
        viewModel.onClickLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController.navigate(
                    FavoriteFragmentDirections.actionFavoriteFragmentToGazalSherItemFragment(it)
                )
                viewModel.onPoemClick(null)
            }
        }
        binding.viewPager.adapter = ViewPagerAdapter(
            requireActivity(),
            listOf(
                GazalFragment.newInstance(PoemType.GAZAL, ParentType.FAVORITE),
                GazalFragment.newInstance(PoemType.SHER, ParentType.FAVORITE),
                RuboiFragment.newInstance(ParentType.FAVORITE)
            )
        )
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = when (pos) {
                1 -> "Шеър"
                2 -> "Рубоёт"
                else -> "Ғазалҳо"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}