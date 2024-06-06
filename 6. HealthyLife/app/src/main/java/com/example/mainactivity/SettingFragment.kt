package com.example.mainactivity

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.mainactivity.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences =
            requireActivity().getSharedPreferences(SHARED_PREFERENCE_KEY, MODE_PRIVATE)

        setListeners()
    }

    private fun setDefaultValue() {
        binding.apply {
            materialSpinnerLanguages.apply {
                setText(language.uppercase())
                setAdapter(
                    ArrayAdapter(
                        requireContext(),
                        R.layout.material_design_spinner_layout,
                        arrayOf("RU", "EN")
                    )
                )
            }
            chooseTheme.setCompoundDrawablesWithIntrinsicBounds(
                if (isThemeNight) R.drawable.ic_baseline_mode_night_24 else R.drawable.ic_baseline_wb_sunny_24,
                0, 0, 0
            )
            themeSwitcher.isChecked = isThemeNight
        }
    }

    private fun setListeners() {
        binding.apply {
            materialSpinnerLanguages.onItemClickListener =
                AdapterView.OnItemClickListener { _, _, p2, _ ->
                    if (language != if (p2 == 0) "ru" else "en") {
                        saveLanguage(
                            sharedPreferences,
                            if (p2 == 0) "ru" else "en"
                        )
                    }
                    requireActivity().recreate()
                }

            themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                AppCompatDelegate.setDefaultNightMode(if (isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO)
                saveThemeMode(sharedPreferences, isChecked)
                chooseTheme.setCompoundDrawablesWithIntrinsicBounds(
                    if (isChecked) R.drawable.ic_baseline_mode_night_24 else R.drawable.ic_baseline_wb_sunny_24,
                    0, 0, 0
                )
            }
            backButton.setOnClickListener {
                getNavController(requireActivity().supportFragmentManager).navigateUp()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setDefaultValue()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
