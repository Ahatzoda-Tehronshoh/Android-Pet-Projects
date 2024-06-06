package com.example.bisanj.screens.menu.main.test

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentTestBinding
import com.example.bisanj.screens.Test
import com.example.bisanj.screens.extensions.navigation.finChildNavController
import com.example.bisanj.screens.extensions.navigation.findTopNavNavController
import com.example.bisanj.screens.extensions.navigation.navigateSafely
import com.example.bisanj.screens.extensions.utils.countOfTests
import com.example.bisanj.screens.extensions.utils.duringOfTest
import com.example.bisanj.screens.extensions.utils.fon
import com.example.bisanj.screens.extensions.utils.showSecond
import com.example.bisanj.screens.menu.main.settingstest.SettingsTestFragmentDirections
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import kotlin.streams.toList

class TestFragment : Fragment() {
    private var _binding: FragmentTestBinding? = null
    private val binding get() = _binding!!

    private val args: TestFragmentArgs by navArgs()

    private var countDownTime: CountDownTimer? = null
    private var counterSeconds = duringOfTest * 60L
    private var timerScrollBar: CountDownTimer? = null
    private var choosingAnswer = 0
    private var counterTest = 1
    private val listOfTests = arrayListOf<Test>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.background = ContextCompat.getDrawable(requireContext(), fon)

        getListOfTests("Kotlin", 8)
        setting()
        setListeners()
        startCountingTime()
    }

    private fun setting() {
        binding.apply {
            decrementTime()
            testProgressBar.apply {
                max = countOfTests * 100
                progress = 0
            }
            tittleText.text = args.subjectName + " синфи " + args.grade
            countTestText.text = "/ $countOfTests"
            counterTestText.text = "Саволи 1"
            val test = listOfTests[0]
            testText.text = test.test
            firstAnswer.text = test.mapOfVariants[1]
            secondAnswer.text = test.mapOfVariants[2]
            thirdAnswer.text = test.mapOfVariants[3]
        }
    }

    private fun setListeners() {
        binding.apply {
            nextTest.setOnClickListener {
                if (choosingAnswer != 0) {
                    //add answer to list
                    listOfTests[counterTest - 1].choosingAnswer = choosingAnswer

                    //if test finished show result
                    if (counterTest == countOfTests) {
                        countDownTime?.cancel()
                        binding.progressBarResult.visibility = VISIBLE
                        it.postDelayed({
                            showResult()
                        }, 1000)
                    } else {
                        startAnimation()

                        // add progress to progressbar with effect
                        binding.testProgressBar.progress = (counterTest - 1) * 100
                        timerScrollBar?.cancel()
                        timerScrollBar = object : CountDownTimer(1000, 20) {
                            override fun onTick(p0: Long) {
                                binding.testProgressBar.progress += 2
                            }

                            override fun onFinish() {
                                binding.testProgressBar.progress = (counterTest - 1) * 100
                            }
                        }.start()

                        binding.counterTestText.text =
                            "Саволи ${++counterTest}"

                        // show knew test
                        it.postDelayed(
                            {
                                unSelectAnotherAnswers()
                                choosingAnswer = 0
                                val test = listOfTests[counterTest - 1]
                                binding.testText.text = test.test
                                binding.firstAnswer.text = test.mapOfVariants[1]
                                binding.secondAnswer.text = test.mapOfVariants[2]
                                binding.thirdAnswer.text = test.mapOfVariants[3]
                            }, 200
                        )

                        // if last test
                        if (counterTest == countOfTests)
                            binding.nextTextView.text = "Result"
                    }
                }
            }

            typeOfShowingTime.setOnClickListener {
                showSecond = !showSecond
            }

            val listener = View.OnClickListener {
                Dialog(requireContext()).apply {
                    setContentView(R.layout.stop_test_dialog)
                    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    findViewById<CardView>(R.id.close_test).setOnClickListener {
                        dismiss()
                        finChildNavController().navigateUp()
                    }
                    findViewById<CardView>(R.id.do_not_close_test).setOnClickListener {
                        dismiss()
                    }
                    show()
                }
            }
            binding.backButton.setOnClickListener(listener)
            binding.backImage.setOnClickListener(listener)

            firstAnswerChooser.setOnClickListener {
                if (choosingAnswer != 1) {
                    unSelectAnotherAnswers()
                    selectedFirstAnswer()
                } else {
                    unSelectedFirstAnswer()
                }
            }

            secondAnswerChooser.setOnClickListener {
                if (choosingAnswer != 2) {
                    unSelectAnotherAnswers()
                    selectedSecondAnswer()
                } else {
                    unSelectedSecondAnswer()
                }
            }

            thirdAnswerChooser.setOnClickListener {
                if (choosingAnswer != 3) {
                    unSelectAnotherAnswers()
                    selectedThirdAnswer()
                } else {
                    unSelectedThirdAnswer()
                }
            }
        }
    }

    private fun decrementTime() {
        binding.timeDownCounter.text =
            if (showSecond || counterSeconds < 60)
                counterSeconds.toString()
            else
                "${
                    if ((counterSeconds / 60) < 10)
                        "0${counterSeconds / 60}"
                    else
                        (counterSeconds / 60)
                }:${
                    if ((counterSeconds % 60) < 10)
                        "0${counterSeconds % 60}"
                    else
                        (counterSeconds % 60)
                }"
    }

    private fun startCountingTime() {
        countDownTime?.cancel()
        countDownTime = object : CountDownTimer(duringOfTest * 60000L, 1000) {
            override fun onTick(p0: Long) {
                counterSeconds--
                decrementTime()
            }

            override fun onFinish() {
                showResult()
            }

        }.start()
    }

    private fun showResult() {
        countDownTime?.cancel()
        finChildNavController().navigate(
            TestFragmentDirections.actionTestFragmentToResultTestFragment(
                listOfTests.toTypedArray()
            )
        )
    }

    private fun getListOfTests(subjectName: String, grade: Int) {
        val listOfLines = getListOfLines("${subjectName + grade}.txt")
        var i = -1
        while (++i < listOfLines.size && listOfTests.size < countOfTests) {
            listOfTests.add(
                Test(
                    listOfLines[i++].trim(),
                    mapOf(
                        1 to listOfLines[i++].trim(),
                        2 to listOfLines[i++].trim(),
                        3 to listOfLines[i++].trim()
                    ),
                    listOfLines[i++].trim().toInt(),
                    0
                )
            )
            Log.d("TAG_test", listOfTests[listOfTests.size-1].toString())
        }
    }

    private fun getListOfLines(fileName: String): List<String> {
        return try {
            BufferedReader(
                InputStreamReader(
                    requireActivity().assets.open(fileName),
                    "utf8"
                )
            ).lines()
                .toList()
        } catch (e: IOException) {
            getListOfLines(fileName)
        }
    }

    private fun startAnimation() {
        binding.mainPartCardView.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.test_close_anim
            )
        )
    }

    private fun unSelectAnotherAnswers() {
        when (choosingAnswer) {
            1 -> unSelectedFirstAnswer()
            2 -> unSelectedSecondAnswer()
            3 -> unSelectedThirdAnswer()
        }
    }

    private fun selectedFirstAnswer() {
        choosingAnswer = 1
        binding.firstAnswerChooser.setCardBackgroundColor(resources.getColor(R.color.background))
        binding.firstAnswer.setTextColor(resources.getColor(R.color.white))
        binding.firstAnswerIcon.showNext()
    }

    private fun unSelectedFirstAnswer() {
        choosingAnswer = 0
        binding.firstAnswerChooser.setCardBackgroundColor(resources.getColor(R.color.fifteen_background))
        binding.firstAnswer.setTextColor(resources.getColor(R.color.black))
        binding.firstAnswerIcon.showNext()
    }

    private fun selectedSecondAnswer() {
        choosingAnswer = 2
        binding.secondAnswerChooser.setCardBackgroundColor(resources.getColor(R.color.background))
        binding.secondAnswer.setTextColor(resources.getColor(R.color.white))
        binding.secondAnswerIcon.showNext()
    }

    private fun unSelectedSecondAnswer() {
        choosingAnswer = 0
        binding.secondAnswerChooser.setCardBackgroundColor(resources.getColor(R.color.fifteen_background))
        binding.secondAnswer.setTextColor(resources.getColor(R.color.black))
        binding.secondAnswerIcon.showNext()
    }

    private fun selectedThirdAnswer() {
        choosingAnswer = 3
        binding.thirdAnswerChooser.setCardBackgroundColor(resources.getColor(R.color.background))
        binding.thirdAnswer.setTextColor(resources.getColor(R.color.white))
        binding.thirdAnswerIcon.showNext()
    }

    private fun unSelectedThirdAnswer() {
        choosingAnswer = 0
        binding.thirdAnswerChooser.setCardBackgroundColor(resources.getColor(R.color.fifteen_background))
        binding.thirdAnswer.setTextColor(resources.getColor(R.color.black))
        binding.thirdAnswerIcon.showNext()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        timerScrollBar?.cancel()
        countDownTime?.cancel()
        timerScrollBar = null
        countDownTime = null
    }
}