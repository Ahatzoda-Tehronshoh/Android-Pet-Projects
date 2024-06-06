package com.example.somoni

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.somoni.databinding.CurrencySpinnerFragmentBinding
import com.example.somoni.databinding.FragmentConvertBinding
import com.example.somoni.setting.ConstVariable
import com.example.somoni.retrofit.NBTResponse
import com.example.somoni.retrofit.ResponseObject
import com.example.somoni.setting.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import java.math.RoundingMode


class ConvertNBTFragment(private val obj: List<NBTResponse>) : DialogFragment() {
    private var firstCurrency = SharedPref.currency
        set(value) {
            field = value
            if (_binding != null) {
                if (binding.firstCurrencyName.text == "TJS") {
                    binding.secondCurrencyName.text = "TJS"
                }
            }
        }


    private val ruble = obj.filter { it.name == "RUB" }[0]
    private val usd = obj.filter { it.name == "USD" }[0]
    private val eur = obj.filter { it.name == "EUR" }[0]

    val listCurrency = listOf(usd, ruble, eur)

    private var _binding: FragmentConvertBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_App_Dialog_FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConvertBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.firstCurrencyName.text = when (SharedPref.currency) {
            0 -> {
                "USD"
            }
            1 -> {
                "RUB"
            }
            else -> {
                "EUR"
            }
        }

        val changer: () -> (String) = {
                if (binding.firstCurrencyName.text == "TJS") {
                    (binding.firstEditText.text.toString().toBigDecimal()
                        .divide(
                            listCurrency[firstCurrency].value.toString().toBigDecimal(),
                            4,
                            RoundingMode.HALF_UP
                        ).toString())
                } else {
                    (binding.firstEditText.text.toString().toBigDecimal()
                        .times(
                            listCurrency[firstCurrency].value.toString().toBigDecimal()
                        ).toString())
                }
        }

        binding.closeButton.setOnClickListener {
            this.dismiss()
        }

        binding.oneCurrencyText.text =
            "1 ${binding.firstCurrencyName.text} = ${listCurrency[firstCurrency].value} ${binding.secondCurrencyName.text}"


        binding.firstEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (text.isNotEmpty()) {
                    binding.resultText.text = changer()
                } else {
                    binding.resultText.text = ""
                }
            }
        }

        binding.qwer5.setOnClickListener {
            val spinnerDialog = BottomSheetDialog(requireContext())
            val bindFragment = CurrencySpinnerFragmentBinding.inflate(LayoutInflater.from(context))

            bindFragment.rubleFullName.text =
                "${ruble.name} (${ruble.full_name})"
            bindFragment.usdFullName.text =
                "${usd.name} (${usd.full_name})"
            bindFragment.eurFullName.text =
                "${eur.name} (${eur.full_name})"

            Picasso
                .get()
                .load(ruble.flag)
                .into(bindFragment.rubleIcon)

            bindFragment.usdIcon.load(usd.flag)
            bindFragment.eurIcon.load(eur.flag)

            bindFragment.rubleChoose.setOnClickListener {
                firstCurrency = 1

                binding.firstCurrencyName.text = "RUB"
                binding.firstEditText.text = binding.firstEditText.text.append("")
                binding.oneCurrencyText.text =
                    "1 ${binding.firstCurrencyName.text} = ${ruble.value} ${binding.secondCurrencyName.text}"

                spinnerDialog.dismiss()
            }

            bindFragment.usdChoose.setOnClickListener {
                firstCurrency = 0

                binding.firstCurrencyName.text = "USD"
                binding.firstEditText.text = binding.firstEditText.text.append("")
                binding.oneCurrencyText.text =
                    "1 ${binding.firstCurrencyName.text} = ${usd.value} ${binding.secondCurrencyName.text}"


                spinnerDialog.dismiss()
            }

            bindFragment.eurChoose.setOnClickListener {
                firstCurrency = 2

                binding.firstCurrencyName.text = "EUR"
                binding.firstEditText.text = binding.firstEditText.text.append("")
                binding.oneCurrencyText.text =
                    "1 ${binding.firstCurrencyName.text} = ${eur.value} ${binding.secondCurrencyName.text}"


                spinnerDialog.dismiss()
            }

            spinnerDialog.setContentView(bindFragment.root)
            spinnerDialog.show()
        }


        binding.changerPlaces.setOnClickListener {
            binding.firstCurrencyName.text = binding.secondCurrencyName.text.also {
                binding.secondCurrencyName.text = binding.firstCurrencyName.text
            }

            if (binding.firstEditText.length() != 0) {
                binding.resultText.text = changer()
            }
        }

        binding.button.visibility = GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}