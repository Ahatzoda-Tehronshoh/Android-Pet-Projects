package com.example.somoni.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.example.somoni.MainActivity
import com.example.somoni.setting.SharedPref
import com.example.somoni.databinding.FragmentOnBoardingThirdBinding
import com.example.somoni.setting.ConstVariable


class OnBoardingThird : Fragment() {
    private var _binding: FragmentOnBoardingThirdBinding? = null
    private val binding: FragmentOnBoardingThirdBinding
        get() = checkNotNull(_binding) {
            "_binding is Null!!!!!"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingThirdBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.onBoardingThirdButton.setOnClickListener {
            startActivity(Intent(view.context, MainActivity::class.java))
            requireActivity().finish()
        }

        ConstVariable.isChecked(binding.simpleMode)
        binding.simpleModeChecked.visibility = VISIBLE

        binding.simpleMode.setOnClickListener {
            ConstVariable.isChecked(binding.simpleMode)
            binding.simpleModeChecked.visibility = VISIBLE

            ConstVariable.isUnChecked(binding.longMode)
            binding.longModeChecked.visibility = View.GONE
            SharedPref.mode = true
        }

        binding.longMode.setOnClickListener {
            ConstVariable.isChecked(binding.longMode)
            binding.longModeChecked.visibility = VISIBLE

            ConstVariable.isUnChecked(binding.simpleMode)
            binding.simpleModeChecked.visibility = View.GONE
            SharedPref.mode = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}