package com.example.messenger.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.messenger.R
import com.example.messenger.databinding.ContactItemLayoutBinding
import com.example.messenger.model.Contact

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ContactViewHolder>(
    object : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem.phoneNumber == newItem.phoneNumber

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean =
            oldItem == newItem
    }
) {

    var onClick: ((Contact) -> (Unit))? = null

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ContactItemLayoutBinding.bind(itemView)

        fun bind(contact: Contact) {
            binding.apply {
                name.text = contact.name
                number.text = contact.phoneNumber

                Glide
                    .with(itemView.context)
                    .load(if (contact.icon != null) Uri.parse(contact.icon) else R.color.teal_700)
                    .into(icon)
            }
        }

        init {
            itemView.setOnClickListener {
                if (adapterPosition >= 0)
                    onClick?.invoke(getItem(adapterPosition))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.contact_item_layout,
            parent,
            false
        )

        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}