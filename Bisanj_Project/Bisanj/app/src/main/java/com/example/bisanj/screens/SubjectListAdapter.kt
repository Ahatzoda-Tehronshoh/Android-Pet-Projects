package com.example.bisanj.screens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.example.bisanj.R
import com.example.bisanj.databinding.SubjectItemLayoutBinding

class SubjectListAdapter(
    private val list: List<Subject>,
    private val onClick: (Subject) -> (Unit)
) : RecyclerView.Adapter<SubjectListAdapter.SubjectViewHolder>() {

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding = SubjectItemLayoutBinding.bind(itemView)

        fun bind(subject: Subject) {
            binding.subjectName.text = subject.name
            binding.subjectIcon.setBackgroundResource(subject.image)
            binding.subjectItem.setOnClickListener {
                onClick(subject)
            }
            if ((list.size - adapterPosition) <= (list.size % 3))
                (binding.subjectItem.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin =
                    150
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder =
        SubjectViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.subject_item_layout, parent, false)
        )

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

}