package com.example.somoni.setting

import android.content.Intent
import android.os.Bundle
import com.example.somoni.MainActivity
import com.example.somoni.databinding.ActivitySettingBinding
import com.example.somoni.language.BaseActivity

class SettingActivity : BaseActivity() {
    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}