package com.tehronshoh.tk_ozarakhsh.adapter

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.data.Poem
import com.tehronshoh.tk_ozarakhsh.databinding.ListItemBinding
import com.tehronshoh.tk_ozarakhsh.utils.PoemDiffUtil

class RuboiAdapter(isSearching: Boolean) :
    ListAdapter<Poem, RuboiAdapter.RuboiViewHolder>(PoemDiffUtil().also {
        it.isSearching = isSearching
    }) {

    var onClickItem: ((Poem, Bitmap) -> (Unit))? = null
    var searchingText: String = ""

    inner class RuboiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
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
            itemView.setOnLongClickListener {
                if (adapterPosition >= 0) onClickItem?.invoke(
                    getItem(adapterPosition), getBitmapFromView(binding.cardLayout)
                )
                true
            }
            itemView.setOnClickListener {
                if (adapterPosition >= 0) onClickItem?.invoke(
                    getItem(adapterPosition), getBitmapFromView(binding.cardLayout)
                )
            }
        }
    }

    fun getBitmapFromView(view: View): Bitmap {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)
        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas) else  //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RuboiViewHolder =
        RuboiViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )

    override fun onBindViewHolder(holder: RuboiViewHolder, position: Int) =
        holder.bind(getItem(position))
}