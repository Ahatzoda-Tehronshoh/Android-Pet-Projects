package com.example.messenger.ui.activity

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.messenger.R
import com.example.messenger.data.getPhoneNumber
import com.example.messenger.databinding.ActivityMainBinding
import com.example.messenger.model.Contact
import com.example.messenger.repository.SharedPreferencesRepository
import com.example.messenger.ui.viewmodel.ActivityViewModel
import com.example.messenger.util.ContextUtils
import com.example.messenger.util.REQUEST_CODE_PERMISSIONS
import com.example.messenger.util.currentUser
import java.util.*

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ActivityViewModel by viewModels()

    private val sharedPreferencesRepository = SharedPreferencesRepository.get()

    override fun attachBaseContext(newBase: Context) {
        val localeToSwitchTo = Locale(SharedPreferencesRepository.get().language)
        val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, localeToSwitchTo)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpThemeMode()

        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment)
                .navController

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility = when (destination.id) {
                R.id.chatFragment -> GONE
                else -> VISIBLE
            }
        }
    }

    private fun setUpThemeMode() {
        AppCompatDelegate.setDefaultNightMode(
            if(sharedPreferencesRepository.themeMode == "light")
                AppCompatDelegate.MODE_NIGHT_NO
            else
                AppCompatDelegate.MODE_NIGHT_YES
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}