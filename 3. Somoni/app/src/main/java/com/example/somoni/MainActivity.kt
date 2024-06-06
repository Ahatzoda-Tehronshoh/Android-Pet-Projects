package com.example.somoni

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.somoni.databinding.ActivityMainBinding
import com.example.somoni.language.BaseActivity

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setupWithNavController((supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment).navController)

    }
}