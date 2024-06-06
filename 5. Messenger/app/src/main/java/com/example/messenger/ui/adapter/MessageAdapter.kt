package com.example.messenger.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.messenger.R
import com.example.messenger.model.Message

class MessageAdapter : ListAdapter<Message, MessageAdapter.MessageViewHolder>(
    object : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem == newItem
    }
) {
    inner class MessageViewHolder(itemView: View) : ViewHolder(itemView) {
        // can use binding, but not with one holder
        private val messageText = itemView.findViewById<TextView>(R.id.message_text)

        fun bind(message: Message) {
            messageText.text = message.text
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position).sent) R.layout.sending_message_item else R.layout.receiving_message_item
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder =
        MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        )

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}