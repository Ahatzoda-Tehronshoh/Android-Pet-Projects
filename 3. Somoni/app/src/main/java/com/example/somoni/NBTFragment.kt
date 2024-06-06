package com.example.somoni

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.somoni.adapters.NBTAdapter
import com.example.somoni.databinding.FragmentNBTBinding
import com.example.somoni.viewmodel.MainViewModel

class NBTFragment : Fragment() {
    val viewModel: MainViewModel by viewModels()

    private var _binding: FragmentNBTBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNBTBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.shimmerLayoutNbt.visibility = VISIBLE
        binding.shimmerLayoutNbt.startShimmer()
        binding.recyclerView.visibility = GONE

        viewModel.getNBTList()

        val recyclerAdapter = NBTAdapter()

        viewModel.nbtLiveData.observe(viewLifecycleOwner) {
            binding.shimmerLayoutNbt.stopShimmer()
            binding.shimmerLayoutNbt.visibility = GONE
            binding.recyclerView.visibility = VISIBLE

            recyclerAdapter.submitList(it)

            binding.convertWithBTNButton.setOnClickListener { _ ->
                ConvertNBTFragment(it.filter { i ->
                    (i.name == "RUB" || i.name == "USD" || i.name == "EUR")
                }).also {
                    it.show(requireActivity().supportFragmentManager, "tag")
                }
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.recyclerView.adapter = recyclerAdapter
        binding.recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.recyclerView.hasFixedSize()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}