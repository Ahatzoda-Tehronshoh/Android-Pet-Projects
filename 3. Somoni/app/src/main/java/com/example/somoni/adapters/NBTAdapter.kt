package com.example.somoni.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.somoni.R
import com.example.somoni.setting.ConstVariable
import com.example.somoni.retrofit.NBTResponse
import com.squareup.picasso.Picasso

class NBTAdapter : ListAdapter<NBTResponse, NBTAdapter.AdapterHolder>(AdapterDiffUtil()) {
    inner class AdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shortName = itemView.findViewById<TextView>(R.id.currencyShortName)
        val fullName = itemView.findViewById<TextView>(R.id.currencyFullName)
        val icon = itemView.findViewById<ImageView>(R.id.iconCurrency)
        val summa = itemView.findViewById<TextView>(R.id.summa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterHolder =
        AdapterHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_nbt_recyclerview,
                    parent,
                    false
                )
        )

    override fun onBindViewHolder(holder: AdapterHolder, position: Int) {
        Picasso
            .get()
            .load(getItem(position).flag)
            .into(holder.icon)

        holder.shortName.text = getItem(position).name
        holder.fullName.text = getItem(position).full_name
        holder.summa.text = ConstVariable.chiselFormat(getItem(position).value.toString())
    }
}

class AdapterDiffUtil : DiffUtil.ItemCallback<NBTResponse>() {
    override fun areItemsTheSame(oldItem: NBTResponse, newItem: NBTResponse): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: NBTResponse, newItem: NBTResponse): Boolean =
        oldItem == newItem

}