package com.example.bisanj.screens.splashscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentSplashScreenBinding
import com.example.bisanj.screens.extensions.navigation.findTopNavNavController
import com.example.bisanj.screens.extensions.navigation.navigateSafely
import com.example.bisanj.screens.extensions.ui.hideActionBar

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater,container,false)
        hideActionBar()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.postDelayed({
            findTopNavNavController().navigate(
                R.id.flowMenuFragment,
                null,
                NavOptions.Builder().setPopUpTo(R.id.splashScreenFragment, true).build()
            )
            //findNavController().navigateSafely(R.id.splashScreenFragment_to_nav_save_name)
        }, 3000)
    }
}