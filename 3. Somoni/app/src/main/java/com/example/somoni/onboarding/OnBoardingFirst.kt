package com.example.somoni.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.example.somoni.R
import com.example.somoni.databinding.FragmentOnBoardingFirstBinding


class OnBoardingFirst : Fragment() {
    private var _binding: FragmentOnBoardingFirstBinding? = null
    private val binding: FragmentOnBoardingFirstBinding
        get() = checkNotNull(_binding) {
            "_binding is Null!!!!!"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.onBoardingFirstButton.setOnClickListener {
            (requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController.navigate(R.id.action_onBoardingFirst_to_onBoardingSecond)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}