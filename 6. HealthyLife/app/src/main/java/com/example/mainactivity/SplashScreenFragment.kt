package com.example.mainactivity

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.example.mainactivity.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!

    private var countDownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val window = requireActivity().window

        window.apply {
            statusBarColor = ResourcesCompat.getColor(resources, R.color.white, null)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        binding.appLogo.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.app_logo_anim
            )
        )

        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(p0: Long) {}

            override fun onFinish() {
                getNavController(requireActivity().supportFragmentManager)
                    .navigate(R.id.action_splashScreenFragment_to_mainFragment)

                window.apply {
                    statusBarColor =
                        ResourcesCompat.getColor(resources, R.color.first_main_color, null)
                    clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
                }
            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer?.cancel()
        countDownTimer = null
        _binding = null
    }

}