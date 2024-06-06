package com.example.bisanj.screens.menu.flowmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentFlowMenuBinding
import com.example.bisanj.screens.extensions.navigation.finChildNavController

class FlowMenuFragment : Fragment() {
    private var _binding: FragmentFlowMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFlowMenuBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomNavigationView
            .setupWithNavController(
                (childFragmentManager
                    .findFragmentById(R.id.menu_fragment_container_view) as NavHostFragment)
                    .navController
            )

        finChildNavController().addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility = if (destination.id in listOf(
                    R.id.settingsFragment,
                    R.id.ratingFragment,
                    R.id.menuFragment
                )
            )
                VISIBLE
            else
                GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigationView.visibility = VISIBLE
    }

}