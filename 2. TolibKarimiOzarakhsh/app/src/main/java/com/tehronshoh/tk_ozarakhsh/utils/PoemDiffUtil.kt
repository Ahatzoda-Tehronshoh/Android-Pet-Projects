package com.tehronshoh.tk_ozarakhsh.utils

import androidx.recyclerview.widget.DiffUtil
import com.tehronshoh.tk_ozarakhsh.data.Poem

class PoemDiffUtil : DiffUtil.ItemCallback<Poem>() {
    var isSearching = false

    override fun areItemsTheSame(oldItem: Poem, newItem: Poem): Boolean =
        !isSearching && oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Poem, newItem: Poem): Boolean =
        !isSearching && oldItem == newItem
}