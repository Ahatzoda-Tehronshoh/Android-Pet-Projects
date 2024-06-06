package com.tehronshoh.tk_ozarakhsh.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.databinding.FragmentSettingBinding
import com.tehronshoh.tk_ozarakhsh.repository.SharedPreferencesRepository


class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val preferences = SharedPreferencesRepository.getInstance()

    private lateinit var listOfFontSize: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOfFontSize = resources.getStringArray(R.array.size_list)

        settingTextSizeSpinner()
        // get data from SharedPreferences and setUp
        setPreferencesAndListeners()
    }

    private fun setPreferencesAndListeners() {
        // 12 - 0,  14 - 1,  16 - 2, 18 - 3, ..., 26 - 7
        binding.sizeSpinner.apply {
            setSelection((preferences.fontSize - 12) / 2)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val changedFontSize = p2 * 2 + 12
                    if (preferences.fontSize != changedFontSize) {
                        preferences.changeFontSize(p2 * 2 + 12)
                        requireActivity().recreate()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }

        binding.notificationSwitcher.apply {
            isChecked = preferences.allowNotification
            setOnCheckedChangeListener { _, isChecked ->
                preferences.changeSettingsNotification(isChecked)
            }
        }

        binding.nightModeSwitcher.apply {
            isChecked = preferences.themeModeIsNight
            setOnCheckedChangeListener { _, isChecked ->
                preferences.changeThemeMode(isChecked)
                AppCompatDelegate.setDefaultNightMode(
                    if (preferences.themeModeIsNight)
                        AppCompatDelegate.MODE_NIGHT_YES
                    else
                        AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }

        binding.helpLayout.setOnClickListener {

            Toast.makeText(
                requireContext(),
                "Номер телефона: +992 92 746 11 10\nПочта: tehronkarimov55@mail.ru",
                Toast.LENGTH_LONG
            ).show()
        }
        binding.telegramIcon.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://telegram.me/tehron_shoh"))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Номер телефона: +992 92 746 11 10\nПочта: tehronkarimov55@mail.ru",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.whatsappIcon.setOnClickListener {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://api.whatsapp.com/send?phone=+992927461110")
                )
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "Номер телефона: +992 92 746 11 10\nПочта: tehronkarimov55@mail.ru",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun settingTextSizeSpinner() {
        binding.sizeSpinner.adapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
            android.R.id.text1,
            listOfFontSize
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}