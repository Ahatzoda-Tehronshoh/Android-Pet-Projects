package com.example.somoni.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.somoni.R
import com.example.somoni.setting.SharedPref
import com.example.somoni.databinding.FragmentOnBoardingSecondBinding
import com.example.somoni.setting.ConstVariable


class OnBoardingSecond : Fragment() {
    private var _binding: FragmentOnBoardingSecondBinding? = null
    private val binding: FragmentOnBoardingSecondBinding
        get() = checkNotNull(_binding) {"_binding is Null!!!!!"}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingSecondBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.onBoardingSecondButton.setOnClickListener {
            (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController.navigate(R.id.action_onBoardingSecond_to_onBoardingThird)
        }

        ConstVariable.isChecked(binding.rubleCardView)
        binding.rubleChecked.visibility = VISIBLE

        binding.rubleCardView.setOnClickListener {
            ConstVariable.isChecked(binding.rubleCardView)
            binding.rubleChecked.visibility = VISIBLE

            when(SharedPref.currency) {
                2 -> {
                    ConstVariable.isUnChecked(binding.eurCardView)
                    binding.eurChecked.visibility = GONE
                }
                0 -> {
                    ConstVariable.isUnChecked(binding.usdCardView)
                    binding.usdChecked.visibility = GONE
                }
            }
            SharedPref.currency = 1
        }

        binding.usdCardView.setOnClickListener {
            ConstVariable.isChecked(binding.usdCardView)
            binding.usdChecked.visibility = VISIBLE

            when(SharedPref.currency) {
                2 -> {
                    ConstVariable.isUnChecked(binding.eurCardView)
                    binding.eurChecked.visibility = GONE
                }
                1 -> {
                    ConstVariable.isUnChecked(binding.rubleCardView)
                    binding.rubleChecked.visibility = GONE
                }
            }
            SharedPref.currency = 0
        }

        binding.eurCardView.setOnClickListener {
            ConstVariable.isChecked(binding.eurCardView)
            binding.eurChecked.visibility = VISIBLE

            when(SharedPref.currency) {
                0 -> {
                    ConstVariable.isUnChecked(binding.usdCardView)
                    binding.usdChecked.visibility = GONE
                }
                1 -> {
                    ConstVariable.isUnChecked(binding.rubleCardView)
                    binding.rubleChecked.visibility = GONE
                }
            }
            SharedPref.currency = 2
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
