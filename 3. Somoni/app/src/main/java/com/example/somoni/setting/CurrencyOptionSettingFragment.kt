package com.example.somoni.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.somoni.setting.ConstVariable
import com.example.somoni.R
import com.example.somoni.databinding.FragmentCurrencyOptionSettingBinding


class CurrencyOptionSettingFragment : Fragment() {
    private var _binding: FragmentCurrencyOptionSettingBinding? = null
    private val binding: FragmentCurrencyOptionSettingBinding
            get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyOptionSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var result = SharedPref.currency
        val navController = (requireActivity().supportFragmentManager.findFragmentById(R.id.setting_nav_host_fragment) as NavHostFragment)
            .navController
        binding.onBoardingSecondButton.setOnClickListener {
            SharedPref.currency = result
            navController.popBackStack()
        }

        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }

        when(result) {
            1 -> {
                ConstVariable.isChecked(binding.rubleCardView)
                binding.rubleChecked.visibility = View.VISIBLE
            }
            2 -> {
                ConstVariable.isChecked(binding.eurCardView)
                binding.eurChecked.visibility = View.VISIBLE
            }
            0 -> {
                ConstVariable.isChecked(binding.usdCardView)
                binding.usdChecked.visibility = View.VISIBLE
            }
        }

        binding.rubleCardView.setOnClickListener {
            ConstVariable.isChecked(binding.rubleCardView)
            binding.rubleChecked.visibility = View.VISIBLE

            when(result) {
                2 -> {
                    ConstVariable.isUnChecked(binding.eurCardView)
                    binding.eurChecked.visibility = View.GONE
                }
                0 -> {
                    ConstVariable.isUnChecked(binding.usdCardView)
                    binding.usdChecked.visibility = View.GONE
                }
            }
            result = 1
        }

        binding.usdCardView.setOnClickListener {
            ConstVariable.isChecked(binding.usdCardView)
            binding.usdChecked.visibility = View.VISIBLE

            when(result) {
                2 -> {
                    ConstVariable.isUnChecked(binding.eurCardView)
                    binding.eurChecked.visibility = View.GONE
                }
                1 -> {
                    ConstVariable.isUnChecked(binding.rubleCardView)
                    binding.rubleChecked.visibility = View.GONE
                }
            }
            result = 0
        }

        binding.eurCardView.setOnClickListener {
            ConstVariable.isChecked(binding.eurCardView)
            binding.eurChecked.visibility = View.VISIBLE

            when(result) {
                0 -> {
                    ConstVariable.isUnChecked(binding.usdCardView)
                    binding.usdChecked.visibility = View.GONE
                }
                1 -> {
                    ConstVariable.isUnChecked(binding.rubleCardView)
                    binding.rubleChecked.visibility = View.GONE
                }
            }
            result = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}