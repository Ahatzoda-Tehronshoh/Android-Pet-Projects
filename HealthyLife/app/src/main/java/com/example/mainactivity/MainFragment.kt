package com.example.mainactivity

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.mainactivity.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private var isAlcoholViewWiden = false
    private var isSmokeViewWiden = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            alcoholControlIcon.setOnClickListener {
                alcoholViewControl(!isAlcoholViewWiden)
            }
            smokeControlIcon.setOnClickListener {
                smokeViewControl(!isSmokeViewWiden)
            }
            pickUpButton.setOnClickListener {
                smokeViewControl(false)
                alcoholViewControl(false)
            }
            settingButton.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_settingFragment)
            }

            smokeAllLifeBtn.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToCalculateFragment(
                        CalculateFragment.SMOKE_ALL_LIFE
                    )
                )
            }
            smokeUntilCurrentTimeBtn.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToCalculateFragment(
                        CalculateFragment.SMOKE_UNTIL_CURRENT_DAY
                    )
                )
            }
            smokeDuringTheTimeBtn.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.actionMainFragmentToCalculateFragment(
                        CalculateFragment.SMOKE_DURING_THE_TIME
                    )
                )
            }

            /*
                alcoholAllLifeBtn.setOnClickListener {
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToAlcoholAllLifeCalculateFragment(
                            CalculateFragment.ALCOHOL_ALL_LIFE
                        )
                    )
                }
                alcoholUntilCurrentTimeBtn.setOnClickListener {
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToAlcoholAllLifeCalculateFragment(
                            CalculateFragment.ALCOHOL_UNTIL_CURRENT_DAY
                        )
                    )
                }
                alcoholDuringTheTimeBtn.setOnClickListener {
                    findNavController().navigate(
                        MainFragmentDirections.actionMainFragmentToAlcoholAllLifeCalculateFragment(
                            CalculateFragment.ALCOHOL_DURING_THE_TIME
                        )
                    )
                }
                */
        }
    }

    private fun alcoholViewControl(isWider: Boolean) {
        if (isAlcoholViewWiden == isWider) return

        isAlcoholViewWiden = isWider
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            if (isAlcoholViewWiden) R.anim.icon_rotate_pi_left else R.anim.icon_rotate_pi_right
        )
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.alcoholCalculatorView.visibility =
                    if (isAlcoholViewWiden) VISIBLE else GONE
            }

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.alcoholControlIcon.startAnimation(anim)
    }

    private fun smokeViewControl(isWider: Boolean) {
        if (isSmokeViewWiden == isWider) return

        isSmokeViewWiden = isWider
        val anim = AnimationUtils.loadAnimation(
            requireContext(),
            if (isSmokeViewWiden) R.anim.icon_rotate_pi_left else R.anim.icon_rotate_pi_right
        )
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {
                binding.smokeCalculatorView.visibility = if (isSmokeViewWiden) VISIBLE else GONE
                binding.scrollView.post {
                    binding.scrollView.fullScroll(FOCUS_DOWN)
                }
            }

            override fun onAnimationEnd(p0: Animation?) {}

            override fun onAnimationRepeat(p0: Animation?) {}
        })
        binding.smokeControlIcon.startAnimation(anim)
    }

    override fun onResume() {
        super.onResume()
        smokeViewControl(false)
        alcoholViewControl(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}