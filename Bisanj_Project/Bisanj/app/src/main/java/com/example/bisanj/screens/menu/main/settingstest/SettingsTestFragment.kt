package com.example.bisanj.screens.menu.main.settingstest

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentSettingTestBinding
import com.example.bisanj.screens.activity.MainActivity
import com.example.bisanj.screens.extensions.navigation.finChildNavController
import com.example.bisanj.screens.extensions.navigation.findTopNavNavController
import com.example.bisanj.screens.extensions.ui.showActionBar
import com.example.bisanj.screens.extensions.utils.countOfTests
import com.example.bisanj.screens.extensions.utils.duringOfTest
import com.example.bisanj.screens.extensions.utils.fon
import com.example.bisanj.screens.menu.flowmenu.FlowMenuFragment
import com.example.bisanj.screens.menu.main.MenuFragment


class SettingsTestFragment : Fragment() {
    private var _binding: FragmentSettingTestBinding? = null
    private val binding get() = _binding!!

    private val arg: SettingsTestFragmentArgs by navArgs()

    private var isChoosedGrade = false
    private var subjectName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subjectName = arg.subjectName

        binding.root.background = ContextCompat.getDrawable(requireContext(), fon)

        binding.timeTestScrollView.scrollTo(5, 10)

        val listener = View.OnClickListener {
            finChildNavController().navigateUp()
        }

        binding.backButton.setOnClickListener(listener)
        binding.backImage.setOnClickListener(listener)

        settingGradeSpinner()

        setCountOfTestColor(R.color.background)
        setDuringOfTestColor(R.color.background)

        switcherSettingsButton()

        binding.startTest.setOnClickListener {
            if (isChoosedGrade)
                finChildNavController().navigate(
                    SettingsTestFragmentDirections.actionSettingsTestFragmentToTestFragment(
                        subjectName,
                        binding.gradeSpinner.text.filterIndexed { _, c -> (c.code in 48..57) }
                            .toString().toInt()
                    )
                )
            else {
                binding.spinnerBox.error = "Синфро интихоб кунед!"
                binding.spinnerBox.isErrorEnabled = true
            }
        }
    }

    private fun settingGradeSpinner() {
        binding.gradeSpinner.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.spinner_grade_item_layout,
                R.id.spinner_item_text,
                resources.getStringArray(R.array.grade_list)
            )
        )

        binding.gradeSpinner.setOnItemClickListener { _, _, _, _ ->
            binding.spinnerBox.isErrorEnabled = false
            isChoosedGrade = true
            binding.spinnerBox.boxBackgroundColor = resources.getColor(R.color.background)
        }
    }

    private fun setCountOfTestColor(colorId: Int) {
        when (countOfTests) {
            10 -> binding.tenTestCount.setCardBackgroundColor(resources.getColor(colorId))
            20 -> binding.twentyTestCount.setCardBackgroundColor(resources.getColor(colorId))
            else -> binding.thirtyTestCount.setCardBackgroundColor(resources.getColor(colorId))
        }
    }

    private fun setDuringOfTestColor(colorId: Int) {
        when (duringOfTest) {
            5 -> binding.fiveMinuteCount.setCardBackgroundColor(resources.getColor(colorId))
            10 -> binding.tenMinuteCount.setCardBackgroundColor(resources.getColor(colorId))
            15 -> binding.fiftyMinuteCount.setCardBackgroundColor(resources.getColor(colorId))
            else -> binding.infinityMinuteCount.setCardBackgroundColor(resources.getColor(colorId))
        }
    }

    private fun switcherSettingsButton() {
        binding.apply {
            tenTestCount.setOnClickListener {
                setCountOfTestColor(R.color.spinner_color_not_selected)
                countOfTests = 10
                setCountOfTestColor(R.color.background)
            }
            twentyTestCount.setOnClickListener {
                setCountOfTestColor(R.color.spinner_color_not_selected)
                countOfTests = 20
                setCountOfTestColor(R.color.background)
            }
            thirtyTestCount.setOnClickListener {
                setCountOfTestColor(R.color.spinner_color_not_selected)
                countOfTests = 30
                setCountOfTestColor(R.color.background)
            }

            fiveMinuteCount.setOnClickListener {
                setDuringOfTestColor(R.color.spinner_color_not_selected)
                duringOfTest = 5
                setDuringOfTestColor(R.color.background)
            }
            tenMinuteCount.setOnClickListener {
                setDuringOfTestColor(R.color.spinner_color_not_selected)
                duringOfTest = 10
                setDuringOfTestColor(R.color.background)
            }
            fiftyMinuteCount.setOnClickListener {
                setDuringOfTestColor(R.color.spinner_color_not_selected)
                duringOfTest = 15
                setDuringOfTestColor(R.color.background)
            }
            infinityMinuteCount.setOnClickListener {
                setDuringOfTestColor(R.color.spinner_color_not_selected)
                duringOfTest = 0
                setDuringOfTestColor(R.color.background)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isChoosedGrade) {
            settingGradeSpinner()
            binding.spinnerBox.boxBackgroundColor =
                ResourcesCompat.getColor(resources, R.color.background, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}