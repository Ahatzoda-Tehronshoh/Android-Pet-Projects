package com.example.somoni.profitable

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.somoni.R
import com.example.somoni.ViewToIntentAsBitmap
import com.example.somoni.adapters.RecyclerAdapter
import com.example.somoni.databinding.FragmentNormalBinding
import com.example.somoni.databinding.ItemRecyclerFullBinding
import com.example.somoni.setting.ConstVariable
import com.example.somoni.retrofit.ResponseObject
import com.example.somoni.setting.SharedPref
import com.example.somoni.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso

class TranslationFromRussiaFragment : Fragment() {
    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentNormalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNormalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.parentView.visibility = View.GONE
        binding.shimmerLayout.startShimmer()

        val recyclerAdapter = RecyclerAdapter(0, requireContext(), childFragmentManager)

        val observer = Observer<List<ResponseObject>> {
            if (it.isEmpty()) {
                binding.notAnswerImageView.visibility = View.VISIBLE
            } else {
                val highest = it[0]

                recyclerAdapter.submitList(it.subList(1, it.size))

                val gradient =
                    ConstVariable.getGradient(
                        it[0].colors!!.firstColor!!,
                        it[0].colors!!.secondColor!!
                    )

                gradient.cornerRadius = 50F

                binding.highestCurrency.background = gradient

                Picasso
                    .get()
                    .load(highest.icon)
                    .into(binding.icon)

                binding.bankName.text = highest.bankName
                binding.buy.text =
                    ConstVariable.chiselFormat(
                        (highest.currency!![0].buyValue!!.toBigDecimal()
                            .times(SharedPref.summa.toBigDecimal())).toString()
                    )
                binding.currency.text = "${SharedPref.summa} ${highest.currency!![0].name}"

                if (SharedPref.mode) {
                    binding.shortMode.visibility = View.VISIBLE
                    binding.longMode.visibility = View.GONE
                    binding.currencyShortMode.text = binding.currency.text
                    binding.buyShortMode.text = binding.buy.text
                } else {
                    binding.shortMode.visibility = View.GONE
                    binding.longMode.visibility = View.VISIBLE
                    binding.sale.text =
                        ConstVariable.chiselFormat(
                            (highest.currency!![0].sellValue!!.toBigDecimal()
                                .times(SharedPref.summa.toBigDecimal())).toString()
                        )
                }
                binding.highestCurrency.setOnClickListener {
                    recyclerAdapter.clickItem(highest)
                }

                binding.parentView.visibility = View.VISIBLE
            }

            binding.shimmerLayout.stopShimmer()
            binding.shimmerLayout.visibility = View.GONE
        }

        viewModel.getTranslationsFromRussianList()
        viewModel.russianLiveData.observe(
            viewLifecycleOwner,
            observer
        )

        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(view.context, RecyclerView.VERTICAL, false)
            this.adapter = recyclerAdapter
            this.hasFixedSize()
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getNormalTranslationList()
            binding.refreshLayout.isRefreshing = false
        }

        binding.shareImageButton.setOnClickListener {
            ViewToIntentAsBitmap().outoCreateFile(context, binding.highestCurrency, "highestcurrencyimage")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}