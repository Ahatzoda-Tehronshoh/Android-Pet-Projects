package com.example.somoni.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import com.example.somoni.MainActivity
import com.example.somoni.R
import com.example.somoni.databinding.FragmentMainSettingBinding

class MainSettingFragment : Fragment() {
    private var _binding: FragmentMainSettingBinding? = null
    private val binding: FragmentMainSettingBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainSettingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.notificationSwitcher.isChecked = SharedPref.notifications
        binding.themeSwitcher.isChecked = SharedPref.theme

        val navController =
            (requireActivity().supportFragmentManager.findFragmentById(R.id.setting_nav_host_fragment) as NavHostFragment)
                .navController

        binding.backButton.setOnClickListener {
            requireActivity().startActivityFromFragment(
                this,
                Intent(context, MainActivity::class.java),
                2
            )
            activity?.finish()
        }

        binding.currencyOptionButton.setOnClickListener {
            navController.navigate(R.id.action_mainSettingFragment_to_currencyOptionSettingFragment)
        }

        binding.currencyModeButton.setOnClickListener {
            navController.navigate(R.id.action_mainSettingFragment_to_modeOptionSettingFragment)
        }

        val spinnerSummaAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listOf(1, 10, 100, 1000)
        )

        binding.summaDefault.adapter = spinnerSummaAdapter

        val spinnerLanguageAdapter = ArrayAdapter(
            requireContext(),
            com.google.android.material.R.layout.support_simple_spinner_dropdown_item, listOf("Русский", "English", "Тоҷикӣ")
        )

        binding.changeLanguage.adapter = spinnerLanguageAdapter

        binding.countOFSumma.setOnClickListener {
            binding.summaDefault.performClick()
        }

        binding.currencyText.text = when (SharedPref.currency) {
            0 -> resources.getString(R.string.Usd)
            1 -> resources.getString(R.string.Ruble)
            else -> resources.getString(R.string.EUR)
        }

        binding.modeText.text =
            if (SharedPref.mode) resources.getString(R.string.SimpleMode) else resources.getString(R.string.LongMode)

        binding.summaDefault.setSelection(spinnerSummaAdapter.getPosition(SharedPref.summa))
        binding.changeLanguage.setSelection(
            spinnerLanguageAdapter.getPosition(
                when (SharedPref.language) {
                    "ru" -> {
                        "Русский"
                    }
                    "en" -> {
                        "English"
                    }
                    else -> {
                        "Тоҷикӣ"
                    }
                }
            )
        )

        binding.summaDefault.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p0 != null) {
                    SharedPref.summa = p0.selectedItem.toString().toInt()
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.changeLanguage.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p0 != null) {
                        val longName = when (SharedPref.language) {
                            "ru" -> {
                                "Русский"
                            }
                            "en" -> {
                                "English"
                            }
                            else -> {
                                "Тоҷикӣ"
                            }
                        }
                        if (longName != p0.selectedItem.toString()) {
                            requireActivity().recreate()
                        }
                        SharedPref.language = when (p0.selectedItem.toString()) {
                            "Русский" -> "ru"
                            "English" -> {
                                "en"
                            }
                            else -> {
                                "tg"
                            }
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}

            }

        binding.writeInTelegram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://telegram.me/tehron_shoh")
            requireActivity().startActivityFromFragment(this, intent, 3)
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, b ->
            SharedPref.theme = b
            if (b)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.notificationSwitcher.setOnCheckedChangeListener { _, b ->
            SharedPref.notifications = b
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}