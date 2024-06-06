package com.example.somoni.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.somoni.profitable.NormalFragment
import com.example.somoni.profitable.TranslationFromRussiaFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when(position) {
            0 -> NormalFragment()
            else -> TranslationFromRussiaFragment()
    }
}