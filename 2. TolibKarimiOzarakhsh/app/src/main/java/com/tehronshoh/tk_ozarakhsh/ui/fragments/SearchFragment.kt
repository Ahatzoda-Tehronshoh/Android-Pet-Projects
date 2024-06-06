package com.tehronshoh.tk_ozarakhsh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.data.ParentType
import com.tehronshoh.tk_ozarakhsh.data.PoemType
import com.tehronshoh.tk_ozarakhsh.databinding.FragmentSearchBinding
import com.tehronshoh.tk_ozarakhsh.viewmodels.ActivityViewModel
import com.tehronshoh.tk_ozarakhsh.viewpager.GazalFragment
import com.tehronshoh.tk_ozarakhsh.viewpager.RuboiFragment
import com.tehronshoh.tk_ozarakhsh.viewpager.ViewPagerAdapter

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var searchingWord = ""

    private val viewModel: ActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireParentFragment()

        settingViewPager()
        searchingSettings()
    }

    private fun searchingSettings() {
        binding.searchEditText.setOnFocusChangeListener { _, b ->
            if (b) {
                binding.cancelSearching.visibility = VISIBLE
                binding.textInputLayout.setStartIconTintList(
                    ContextCompat.getColorStateList(
                        requireContext(), R.color.first_color
                    )
                )
            } else {
                binding.cancelSearching.visibility = GONE
                binding.textInputLayout.setStartIconTintList(
                    ContextCompat.getColorStateList(
                        requireContext(), R.color.gray_500
                    )
                )
            }
        }
        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            searchingWord = text.toString()
            viewModel.search(text.toString(), viewModel.currentSearchingPoemType)
        }
        if (searchingWord.isNotEmpty()) binding.textInputLayout.requestFocus()

        binding.cancelSearching.setOnClickListener {
            binding.searchEditText.setText("")
            binding.textInputLayout.clearFocus()
            viewModel.search("")
        }
        viewModel.onClickLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController.navigate(
                    SearchFragmentDirections.actionSearchFragmentToGazalSherItemFragment(it)
                )
                viewModel.onPoemClick(null)
            }
        }
    }

    private fun settingViewPager() {
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(
                requireActivity(), listOf(
                    GazalFragment.newInstance(PoemType.GAZAL, ParentType.SEARCH),
                    GazalFragment.newInstance(PoemType.SHER, ParentType.SEARCH),
                    RuboiFragment.newInstance(ParentType.SEARCH)
                )
            )

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.currentSearchingPoemType = PoemType.values()[position]
                    if (searchingWord.isNotEmpty())
                        viewModel.search(searchingWord)
                }
            })
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            tab.text = when (pos) {
                1 -> "Шеър"
                2 -> "Рубоёт"
                else -> "Ғазалҳо"
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        if (searchingWord.isNotEmpty())
            binding.textInputLayout.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}