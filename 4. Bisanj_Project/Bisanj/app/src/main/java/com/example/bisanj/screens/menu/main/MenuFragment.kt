package com.example.bisanj.screens.menu.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bisanj.databinding.FragmentMenuBinding
import com.example.bisanj.screens.SubjectListAdapter
import com.example.bisanj.screens.extensions.navigation.finChildNavController
import com.example.bisanj.screens.extensions.navigation.findTopNavNavController
import com.example.bisanj.screens.extensions.utils.SUBJECT_LIST

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.subjectsList.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.subjectsList.adapter = SubjectListAdapter(SUBJECT_LIST) {
            finChildNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToSettingsTestFragment(it.name)
            )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}