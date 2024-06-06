package com.example.mainactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.FOCUS_DOWN
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.mainactivity.databinding.FragmentCalculateBinding
import com.example.mainactivity.databinding.YearPickerBottomSheetLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlin.math.roundToInt

class CalculateFragment : Fragment() {
    private var _binding: FragmentCalculateBinding? = null
    private val binding get() = _binding!!

    private val args: CalculateFragmentArgs by navArgs()

    private var showResult: (a: Double, x: Double) -> (Unit) = { _, _ -> }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalculateBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefaultSettings(args.indexOfFragment)
        setListeners()
    }

    private fun setDefaultSettings(key: Int) {
        ContextCompat.getColorStateList(
            requireContext(),
            R.drawable.text_input_layout_stroke_color
        ).also {
            if (it != null) {
                binding.apply {
                    boxCostOfOneParkSmoke.setBoxStrokeColorStateList(it)
                    boxCountInPackSmoke.setBoxStrokeColorStateList(it)
                    boxCountOnDaySmoke.setBoxStrokeColorStateList(it)
                    boxExperienceSmoke.setBoxStrokeColorStateList(it)
                    boxSalarySmoke.setBoxStrokeColorStateList(it)
                    boxHowYearYouWantToLiveSmoke.setBoxStrokeColorStateList(it)
                }
            }
        }


        when (key) {
            SMOKE_ALL_LIFE -> {
                binding.boxHowYearYouWantToLiveSmoke.editText?.hint =
                    getString(R.string.how_year_you_want_to_live)
                showResult = { it, _ ->
                    val months = ((it - it.toInt()) * 12).toInt()
                    binding.resultText.text =
                        (resources.getString(R.string.result)
                                + getString(R.string.in_life_you)
                                + it.toInt()
                                + getString(R.string.year_let)
                                + (if (months != 0) (getString(R.string.and) + months.toString() + getString(
                            R.string.months
                        )) else "")
                                + getString(R.string.free_worked_for_smoke))
                }
            }
            SMOKE_UNTIL_CURRENT_DAY -> {
                binding.boxHowYearYouWantToLiveSmoke.editText?.hint =
                    getString(R.string.how_old_are_you)
                showResult = { it, _ ->
                    val months = ((it - it.toInt()) * 12).toInt()
                    binding.resultText.text =
                        (resources.getString(R.string.result)
                                + getString(R.string.until_this_year_you)
                                + it.toInt()
                                + getString(R.string.year_let)
                                + (if (months != 0) (getString(R.string.and) + months.toString() + getString(
                            R.string.months
                        )) else "")
                                + getString(R.string.free_worked_for_smoke))
                }
            }
            SMOKE_DURING_THE_TIME -> {
                binding.boxExperienceSmoke.visibility = GONE
                binding.boxHowYearYouWantToLiveSmoke.editText?.hint =
                    getString(R.string.count_of_days)
                showResult = { result, spendingSalary ->
                    binding.resultText.text =
                        (resources.getString(R.string.result)
                                + getString(R.string.during_this_time_you_spend)
                                + ((result * 10.0).roundToInt() / 10.0)
                                + "($spendingSalary)"
                                + getString(R.string.prot_your_salary))
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            backButton.setOnClickListener {
                getNavController(requireActivity().supportFragmentManager).popBackStack()
            }
            boxExperienceSmoke.apply {
                editText?.setOnFocusChangeListener { _, b ->
                    if (b) isErrorEnabled = false
                }
                setStartIconOnClickListener {
                    isErrorEnabled = false
                    showNumberPickerBottomSheetDialog(
                        editText?.text.toString().toIntOrNull() ?: 1
                    ) {
                        editText?.setText(it.toString())
                        isEndIconVisible = true
                    }
                }
            }

            boxHowYearYouWantToLiveSmoke.apply {
                editText?.setOnFocusChangeListener { _, b ->
                    if (b) isErrorEnabled = false
                }
                setStartIconOnClickListener {
                    isErrorEnabled = false
                    showNumberPickerBottomSheetDialog(
                        editText?.text.toString().toIntOrNull() ?: 1
                    ) {
                        editText?.setText(it.toString())
                        isEndIconVisible = true
                    }
                }
            }

            boxCostOfOneParkSmoke.editText?.setOnFocusChangeListener { _, b ->
                if (b) boxCostOfOneParkSmoke.isErrorEnabled = false
            }
            boxCountInPackSmoke.editText?.setOnFocusChangeListener { _, b ->
                if (b) boxCountInPackSmoke.isErrorEnabled = false
            }
            boxCountOnDaySmoke.editText?.setOnFocusChangeListener { _, b ->
                if (b) boxCountOnDaySmoke.isErrorEnabled = false
            }
            boxSalarySmoke.editText?.setOnFocusChangeListener { _, b ->
                if (b) boxSalarySmoke.isErrorEnabled = false
            }


            calculateButton.setOnClickListener {
                if (isSetNumber()) {
                    val x = boxCountOnDaySmoke.editText?.text.toString().toInt()
                    val x1 = boxCountInPackSmoke.editText?.text.toString().toInt()
                    val z = boxCostOfOneParkSmoke.editText?.text.toString().toDouble()
                    val y = boxSalarySmoke.editText?.text.toString().toDouble()
                    val v = boxHowYearYouWantToLiveSmoke.editText?.text.toString().toInt()

                    if (args.indexOfFragment == SMOKE_DURING_THE_TIME) {
                        val xg = ((x * v) / x1.toDouble()) * z
                        showResult((xg * 100) / y, xg)
                    } else {
                        val n = (12 / (((((30 * x) / x1.toDouble()) * z) / y) * 12))
                        val r = boxExperienceSmoke.editText?.text.toString().toInt()
                        showResult(((v - r) / n), 0.0)
                    }
                    binding.scrollView.fullScroll(FOCUS_DOWN)
                }
            }
        }
    }

    private fun isSetNumber(): Boolean {
        binding.apply {
            var result = true
            if (boxCountOnDaySmoke.editText?.text.toString().toIntOrNull() == null) {
                boxCountOnDaySmoke.apply {
                    error = getString(R.string.did_not_add_text)
                    isErrorEnabled = true
                }
                result = false
            }
            if (boxCountInPackSmoke.editText?.text.toString().toIntOrNull() == null) {
                boxCountInPackSmoke.apply {
                    error = getString(R.string.did_not_add_text)
                    isErrorEnabled = true
                }
                result = false
            }
            if (boxCostOfOneParkSmoke.editText?.text.toString().toDoubleOrNull() == null) {
                boxCostOfOneParkSmoke.apply {
                    error = getString(R.string.did_not_add_text)
                    isErrorEnabled = true
                }
                result = false
            }
            if (boxSalarySmoke.editText?.text.toString().toDoubleOrNull() == null) {
                boxSalarySmoke.apply {
                    error = getString(R.string.did_not_add_text)
                    isErrorEnabled = true
                }
                result = false
            }
            if (args.indexOfFragment != SMOKE_DURING_THE_TIME && boxExperienceSmoke.editText?.text.toString()
                    .toIntOrNull() == null
            ) {
                boxExperienceSmoke.apply {
                    error = getString(R.string.did_not_add_text)
                    isErrorEnabled = true
                }
                result = false
            }
            if (boxHowYearYouWantToLiveSmoke.editText?.text.toString().toIntOrNull() == null) {
                boxHowYearYouWantToLiveSmoke.apply {
                    error = getString(R.string.did_not_add_text)
                    isErrorEnabled = true
                }
                result = false
            }
            if (args.indexOfFragment != SMOKE_DURING_THE_TIME && result && boxExperienceSmoke.editText?.text.toString()
                    .toInt() >= boxHowYearYouWantToLiveSmoke.editText?.text.toString().toInt()
            ) {
                boxHowYearYouWantToLiveSmoke.apply {
                    error = getString(R.string.did_not_right_text)
                    isErrorEnabled = true
                }
                boxExperienceSmoke.apply {
                    error = getString(R.string.did_not_right_text)
                    isErrorEnabled = true
                }
                result = false
            }
            return result
        }
    }

    private fun showNumberPickerBottomSheetDialog(currentValue: Int, listener: (Int) -> (Unit)) {
        val bottomSheet = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialogTheme
        )
        val bottomSheetBinding =
            YearPickerBottomSheetLayoutBinding.inflate(bottomSheet.layoutInflater)
        bottomSheet.setContentView(bottomSheetBinding.root)

        bottomSheetBinding.yearPicker.apply {
            minValue = 1
            maxValue = if (args.indexOfFragment == SMOKE_DURING_THE_TIME) 31 else 125
            value = currentValue
            listener(currentValue)
            setOnValueChangedListener { _, _, _ ->
                listener(value)
            }
        }

        bottomSheet.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val SMOKE_ALL_LIFE = 0
        const val SMOKE_UNTIL_CURRENT_DAY = 1
        const val SMOKE_DURING_THE_TIME = 2
    }
}