package com.example.bisanj.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bisanj.R
import com.example.bisanj.databinding.ItemListTestResultBinding

class TestListAdapter(
    private val list: List<Test>
) : RecyclerView.Adapter<TestListAdapter.TestViewHolder>() {

    inner class TestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemListTestResultBinding.bind(itemView)

        fun bind(test: Test, position: Int) {
            binding.apply {
                counterTest.text = "${(position + 1)}. "
                this.test.text = test.test
                choosingAnswer.text =
                    "${test.mapOfVariants[test.choosingAnswer]}" + if (test.choosingAnswer != test.answer) {
                        choosingAnswer.setTextColor(itemView.context.resources.getColor(R.color.gradient_start_color))
                        " - нодуруст"
                    } else {
                        choosingAnswer.setTextColor(itemView.context.resources.getColor(R.color.background))
                        " - дуруст"
                    }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder =
        TestViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_list_test_result, parent, false)
        )

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
}