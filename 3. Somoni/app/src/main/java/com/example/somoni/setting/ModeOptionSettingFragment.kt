package com.example.somoni.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.somoni.setting.ConstVariable
import com.example.somoni.R
import com.example.somoni.databinding.FragmentModeOptionSettingBinding

class ModeOptionSettingFragment : Fragment() {
    private var _binding: FragmentModeOptionSettingBinding? = null
    private val binding: FragmentModeOptionSettingBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModeOptionSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var result = SharedPref.mode
        val navController = (requireActivity().supportFragmentManager.findFragmentById(R.id.setting_nav_host_fragment) as NavHostFragment)
            .navController

        binding.onBoardingThirdButton.setOnClickListener {
            SharedPref.mode = result
            navController.popBackStack()
        }

        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }

        when {
            result -> {
                ConstVariable.isChecked(binding.simpleMode)
                binding.simpleModeChecked.visibility = View.VISIBLE
            }
            else -> {
                ConstVariable.isChecked(binding.longMode)
                binding.longModeChecked.visibility = View.VISIBLE
            }
        }

        binding.simpleMode.setOnClickListener {
            ConstVariable.isChecked(binding.simpleMode)
            binding.simpleModeChecked.visibility = View.VISIBLE

            ConstVariable.isUnChecked(binding.longMode)
            binding.longModeChecked.visibility = View.GONE
            result = true
        }

        binding.longMode.setOnClickListener {
            ConstVariable.isChecked(binding.longMode)
            binding.longModeChecked.visibility = View.VISIBLE

            ConstVariable.isUnChecked(binding.simpleMode)
            binding.simpleModeChecked.visibility = View.GONE
            result = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}