package com.example.bisanj.screens.menu.rating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bisanj.databinding.FragmentRatingBinding


class RatingFragment : Fragment() {
   lateinit var binding: FragmentRatingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRatingBinding.inflate(inflater,container,false)
        return binding.root
    }
}