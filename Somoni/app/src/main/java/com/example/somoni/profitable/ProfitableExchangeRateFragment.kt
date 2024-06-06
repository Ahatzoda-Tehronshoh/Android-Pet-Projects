package com.example.somoni.profitable

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.somoni.adapters.ViewPagerAdapter
import com.example.somoni.databinding.FragmentProfitableExchangeRateBinding
import com.example.somoni.setting.SettingActivity
import com.google.android.material.tabs.TabLayoutMediator

class ProfitableExchangeRateFragment : Fragment() {
    private var _binding: FragmentProfitableExchangeRateBinding? = null
    private val binding: FragmentProfitableExchangeRateBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfitableExchangeRateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = listOf("Обычный", "Переводы из РФ")[position]
        }.attach()

        binding.settingButton.setOnClickListener {
            requireActivity().startActivityFromFragment(this, Intent(context, SettingActivity::class.java), 1)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}