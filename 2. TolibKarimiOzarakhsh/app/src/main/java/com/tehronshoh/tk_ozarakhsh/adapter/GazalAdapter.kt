package com.tehronshoh.tk_ozarakhsh.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.databinding.ListItemBinding
import com.tehronshoh.tk_ozarakhsh.utils.PoemDiffUtil

class GazalAdapter(isSearching: Boolean) :
    ListAdapter<Poem, GazalAdapter.GazalViewHolder>(PoemDiffUtil().also {
        it.isSearching = isSearching
    }) {

    var onItemClickListener: ((Poem) -> (Unit))? = null
    var searchingText: String = ""

    inner class GazalViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)

        fun bind(poem: Poem) {
            binding.text.text = if (searchingText.isNotEmpty() && poem.title?.lowercase()
                    ?.contains(searchingText.lowercase()) == true
            )
                setSpanAffect(poem.title)
            else
                poem.title
        }

        private fun setSpanAffect(title: String): SpannableString {
            val spannable = SpannableString(title)

            val startIndex = getStartIndexSubString(title.lowercase(), searchingText.lowercase())

            spannable.setSpan(
                BackgroundColorSpan(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.transparent_gray_70_fix
                    )
                ),
                startIndex,
                startIndex + searchingText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            return spannable
        }

        private fun getStartIndexSubString(str: String, subString: String): Int {
            for (index in 0..(str.length - subString.length)) {
                if (str.substring(index, index + subString.length) == subString) {
                    return index
                }
            }

            return 0
        }

        init {
            binding.root.setOnClickListener {
                if (adapterPosition >= 0)
                    onItemClickListener?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GazalViewHolder =
        GazalViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: GazalViewHolder, position: Int) =
        holder.bind(getItem(position))
}