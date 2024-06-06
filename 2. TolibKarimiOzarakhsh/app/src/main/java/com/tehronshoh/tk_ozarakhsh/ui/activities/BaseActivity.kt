package com.tehronshoh.tk_ozarakhsh.ui.activities

import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.repository.SharedPreferencesRepository

open class BaseActivity : AppCompatActivity() {

    private val sharedPreferences = SharedPreferencesRepository.getInstance()

    override fun onStart() {
        setTheme(getSavingTheme())

        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = ContextCompat.getColor(
            this,
            R.color.status_bar_color
        )

        AppCompatDelegate.setDefaultNightMode(
            if (sharedPreferences.themeModeIsNight) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onStart()
    }

    private fun getSavingTheme(): Int = when (sharedPreferences.fontSize) {
        12 -> R.style.TextViewStyle12
        14 -> R.style.TextViewStyle14
        16 -> R.style.TextViewStyle16
        18 -> R.style.TextViewStyle18
        20 -> R.style.TextViewStyle20
        22 -> R.style.TextViewStyle22
        24 -> R.style.TextViewStyle24
        26 -> R.style.TextViewStyle26
        else -> R.style.Theme_TolibKarimiOzarakhsh
    }
}