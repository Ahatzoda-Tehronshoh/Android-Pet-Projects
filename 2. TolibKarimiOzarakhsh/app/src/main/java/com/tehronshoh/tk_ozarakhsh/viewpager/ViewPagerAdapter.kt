package com.tehronshoh.tk_ozarakhsh.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fa: FragmentActivity,
    private val listOfFragments: List<Fragment>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment =
        when(position) {
            0 -> listOfFragments[0]
            1 -> listOfFragments[1]
            2 -> listOfFragments[2]
            else -> listOfFragments[0]
        }
}