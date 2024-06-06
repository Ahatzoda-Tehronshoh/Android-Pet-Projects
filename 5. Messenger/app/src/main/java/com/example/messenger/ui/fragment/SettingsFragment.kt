package com.example.messenger.ui.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.messenger.databinding.FragmentSettingsBinding
import com.example.messenger.repository.SharedPreferencesRepository
import com.example.messenger.util.ContextUtils
import com.example.messenger.util.currentUser
import java.util.*


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var mStartForResult: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> {
                if (it.resultCode == Activity.RESULT_OK) {
                    binding.image.setImageURI(it.data?.data)
                    SharedPreferencesRepository.get()
                        .changeUserImage(it.data?.data.toString())
                }
            })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setData()
        setUpListeners()
    }

    private fun setUpListeners() {
        val shPref = SharedPreferencesRepository.get()
        binding.apply {
            changeLanguage.setOnClickListener {
                shPref.changeLanguage(
                    if(shPref.language == "ru")
                        "en"
                    else
                        "ru"
                )

                requireActivity().recreate()
            }

            changeThemeMode.setOnClickListener {
                shPref.changeThemeMode(
                    if (shPref.themeMode == "light")
                        "night"
                    else
                        "light"
                )

                AppCompatDelegate.setDefaultNightMode(
                    if (shPref.themeMode == "light")
                        AppCompatDelegate.MODE_NIGHT_NO
                    else
                        AppCompatDelegate.MODE_NIGHT_YES
                )
            }

            changeImage.setOnClickListener {
                val gallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                mStartForResult.launch(gallery)
            }
        }
    }

    private fun setData() {
        binding.apply {
            name.text = currentUser.name
            phoneNumber.text = currentUser.phoneNumber
            if (currentUser.icon != null)
                Glide
                    .with(requireContext())
                    .load(Uri.parse(currentUser.icon))
                    .centerCrop()
                    .into(binding.image)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}