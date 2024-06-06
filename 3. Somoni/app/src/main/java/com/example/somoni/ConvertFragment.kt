package com.example.somoni

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.somoni.databinding.CurrencySpinnerFragmentBinding
import com.example.somoni.databinding.FragmentConvertBinding
import com.example.somoni.setting.ConstVariable
import com.example.somoni.retrofit.ResponseObject
import com.example.somoni.setting.SharedPref
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.squareup.picasso.Picasso
import java.math.RoundingMode


class ConvertFragment(private val obj: ResponseObject) : DialogFragment() {
    var firstCurrency = SharedPref.currency
        set(value) {
            field = value
            if (_binding != null) {
                if (binding.firstCurrencyName.text == "TJS") {
                    binding.secondCurrencyName.text = "TJS"
                }
            }
        }

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
        if (obj.currency!!.size > 2) {
            binding.firstCurrencyName.text = when (firstCurrency) {
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

            binding.oneCurrencyText.text =
                "1 ${binding.firstCurrencyName.text} = ${obj!!.currency!![firstCurrency].buyValue} ${binding.secondCurrencyName.text}"
        } else {
            binding.firstCurrencyName.text = "RUB"
            binding.oneCurrencyText.text =
                "1 ${binding.firstCurrencyName.text} = ${obj!!.currency!![0].buyValue} ${binding.secondCurrencyName.text}"
        }

        val changer: () -> (String) = {
            val x = if (obj.currency!!.size > 2) firstCurrency else 0
            if (binding.firstCurrencyName.text == "TJS") {
                (binding.firstEditText.text.toString().toBigDecimal()
                    .divide(
                        obj.currency!![x].buyValue.toString()
                            .toBigDecimal(),
                        4,
                        RoundingMode.HALF_UP
                    ).toString())
            } else {
                (binding.firstEditText.text.toString().toBigDecimal()
                    .times(
                        obj.currency!![x].buyValue.toString()
                            .toBigDecimal()
                    ).toString())
            }
        }

        binding.closeButton.setOnClickListener {
            this.dismiss()
        }

        binding.firstEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.isNotEmpty()) {
                    binding.resultText.text = changer()
                } else {
                    binding.resultText.text = ""
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })


        if (obj.currency!!.size > 2) {
            binding.qwer5.setOnClickListener {
                val spinnerDialog = BottomSheetDialog(context!!)
                val bindFragment =
                    CurrencySpinnerFragmentBinding.inflate(LayoutInflater.from(context))

                bindFragment.rubleFullName.text =
                    "${obj.currency!![1].name} (${obj.currency!![1].fullName})"
                bindFragment.usdFullName.text =
                    "${obj.currency!![0].name} (${obj.currency!![0].fullName})"
                bindFragment.eurFullName.text =
                    "${obj.currency!![2].name} (${obj.currency!![2].fullName})"

                Picasso
                    .get()
                    .load(obj.currency!![1].flag)
                    .into(bindFragment.rubleIcon)

                bindFragment.usdIcon.load(obj.currency!![0].flag)
                bindFragment.eurIcon.load(obj.currency!![2].flag)

                bindFragment.rubleChoose.setOnClickListener {
                    firstCurrency = 1

                    binding.firstCurrencyName.text = "RUB"
                    binding.firstEditText.text = binding.firstEditText.text.append("")
                    binding.oneCurrencyText.text =
                        "1 ${binding.firstCurrencyName.text} = ${obj!!.currency!![firstCurrency].buyValue} ${binding.secondCurrencyName.text}"

                    spinnerDialog.dismiss()
                }

                bindFragment.usdChoose.setOnClickListener {
                    firstCurrency = 0

                    binding.firstCurrencyName.text = "USD"
                    binding.firstEditText.text = binding.firstEditText.text.append("")
                    binding.oneCurrencyText.text =
                        "1 ${binding.firstCurrencyName.text} = ${obj!!.currency!![firstCurrency].buyValue} ${binding.secondCurrencyName.text}"


                    spinnerDialog.dismiss()
                }

                bindFragment.eurChoose.setOnClickListener {
                    firstCurrency = 2

                    binding.firstCurrencyName.text = "EUR"
                    binding.firstEditText.text = binding.firstEditText.text.append("")
                    binding.oneCurrencyText.text =
                        "1 ${binding.firstCurrencyName.text} = ${obj!!.currency!![firstCurrency].buyValue} ${binding.secondCurrencyName.text}"


                    spinnerDialog.dismiss()
                }

                spinnerDialog.setContentView(bindFragment.root)
                spinnerDialog.show()
            }
        }

        binding.changerPlaces.setOnClickListener {
            binding.firstCurrencyName.text = binding.secondCurrencyName.text.also {
                binding.secondCurrencyName.text = binding.firstCurrencyName.text
            }

            if (binding.firstEditText.length() != 0) {
                binding.resultText.text = changer()
            }
        }


        if (obj.androidLink!!.contains("play.google")) {
            binding.buttonText.text = "Скачать приложение ${obj.bankName}"
        } else {
            binding.buttonText.text = "Перейти на сайт ${obj.bankName}"
        }

        binding.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.apply {
                data = Uri.parse(obj.androidLink)
            }
            requireActivity().startActivityFromFragment(this, intent, 4)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}