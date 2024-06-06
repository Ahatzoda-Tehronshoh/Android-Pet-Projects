package com.example.bisanj.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.navArgs
import com.example.bisanj.R
import com.example.bisanj.databinding.FragmentResultTestBinding
import com.example.bisanj.databinding.FragmentTestBinding
import com.example.bisanj.screens.activity.MainActivity
import com.example.bisanj.screens.extensions.navigation.finChildNavController
import com.example.bisanj.screens.extensions.navigation.findTopNavNavController
import com.example.bisanj.screens.extensions.navigation.overrideOnBackPressed
import com.example.bisanj.screens.extensions.utils.countOfTests
import com.example.bisanj.screens.extensions.utils.fon
import com.example.bisanj.screens.menu.main.test.TestFragment
import com.example.bisanj.screens.savenamescreen.start.StartFragmentDirections

class ResultTestFragment : Fragment() {
    private var _binding: FragmentResultTestBinding? = null
    private val binding get() = _binding!!

    private val args: ResultTestFragmentArgs by navArgs()

    private var counterOfRightAnswers = 0
    private lateinit var testList: List<Test>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultTestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.root.background = ContextCompat.getDrawable(requireContext(), fon)

        overrideOnBackPressed {
            finChildNavController().navigate(R.id.action_resultTestFragment_to_menuFragment)
        }

        testList = args.listOfTestsWithAnswers.toList()
        testList.forEach { if (it.choosingAnswer == it.answer) counterOfRightAnswers++ }

        binding.countOfRightAnswers.text = counterOfRightAnswers.toString()
        binding.countOfTests.text = " / $countOfTests"

        binding.answersDiagramProgressBar.apply {
            max = countOfTests
            progress = countOfTests - counterOfRightAnswers
        }

        binding.menuButton.setOnClickListener {
            finChildNavController().navigate(R.id.action_resultTestFragment_to_menuFragment)
        }

        binding.listOfTests.adapter = TestListAdapter(testList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}