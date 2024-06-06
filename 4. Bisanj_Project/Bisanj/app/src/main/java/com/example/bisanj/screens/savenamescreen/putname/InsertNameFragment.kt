package com.example.bisanj.screens.savenamescreen.putname

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentInsertNameBinding
import com.example.bisanj.screens.extensions.navigation.findTopNavNavController
import com.example.bisanj.screens.extensions.ui.showActionBar

class InsertNameFragment : Fragment() {
    lateinit var binding: FragmentInsertNameBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsertNameBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.saveName.setOnClickListener {
            findTopNavNavController().navigate(R.id.flowMenuFragment,null,
                NavOptions.Builder().setPopUpTo(R.id.nav_save_name,true).build())
        }
    }
    override fun onResume() {
        super.onResume()
        showActionBar()
    }
}