package com.example.bisanj

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.bisanj.databinding.FragmentSettingFonChangeBinding
import com.example.bisanj.screens.extensions.navigation.finChildNavController
import com.example.bisanj.screens.extensions.utils.SHARED_PREFERENCES_KEY
import com.example.bisanj.screens.extensions.utils.fon

class SettingFonChangeFragment : Fragment() {
    private var _binding: FragmentSettingFonChangeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingFonChangeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSelectedDrawable(fon)
        setUpListeners()
    }

    private fun setSelectedDrawable(fonKey: Int) {
        binding.apply {
            when (fon) {
                BLUE -> {
                    blueImageView.foreground = null
                }
                ORANGE -> {
                    orangeImageView.foreground = null
                }
                PINK -> {
                    pinkImageView.foreground = null
                }
                GREEN -> {
                    greenImageView.foreground = null
                }
            }
            if (fonKey != fon) {
                fon = fonKey
                requireActivity().getSharedPreferences(SHARED_PREFERENCES_KEY, MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREFERENCES_FON_KEY, fon)
                    .commit()
            }
            val selectedDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_selceted_fon_foreground)
            when (fonKey) {
                BLUE -> {
                    blueImageView.foreground = selectedDrawable
                }
                ORANGE -> {
                    orangeImageView.foreground = selectedDrawable
                }
                PINK -> {
                    pinkImageView.foreground = selectedDrawable
                }
                GREEN -> {
                    greenImageView.foreground = selectedDrawable
                }
            }
        }
    }

    private fun setUpListeners() {
        binding.apply {
            backButton.setOnClickListener {
                finChildNavController().popBackStack()
            }
            blue.setOnClickListener {
                setSelectedDrawable(BLUE)
            }
            orange.setOnClickListener {
                setSelectedDrawable(ORANGE)
            }
            pink.setOnClickListener {
                setSelectedDrawable(PINK)
            }
            green.setOnClickListener {
                setSelectedDrawable(GREEN)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SHARED_PREFERENCES_FON_KEY = "shared preferences fon key"
        const val BLUE = R.drawable.blue_background_fon
        const val ORANGE = R.drawable.orange_background_fon
        const val PINK = R.drawable.pink_background_fon
        const val GREEN = R.drawable.green_background_fon
    }

}