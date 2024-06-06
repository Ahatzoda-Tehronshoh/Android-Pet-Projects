package com.example.bisanj.screens.savenamescreen.start

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentStartBinding
import com.example.bisanj.screens.extensions.navigation.navigateSafely
import com.example.bisanj.screens.extensions.ui.hideActionBar
import com.example.bisanj.screens.extensions.ui.showActionBar


class StartFragment : Fragment() {
    lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStartBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toinsertName.setOnClickListener {
            findNavController().navigateSafely(R.id.startFragment_to_insertNameFragment)
        }
    }
    override fun onResume() {
        super.onResume()
        hideActionBar()
    }
}