package com.example.tubespab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Item

class HomeAdapter(
    private val dataList: List<Item>
): RecyclerView.Adapter<HomeAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvExpired: TextView = itemView.findViewById(R.id.tvExpired)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.almost_expired_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: HomeAdapter.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        currentItem.icon?.let {
            val iconRes = it.toIntOrNull() ?: R.drawable.stat_view
            holder.ivIcon.setImageResource(iconRes)
        }
        holder.tvItemName.text = currentItem.name
        holder.tvExpired.text = currentItem.expiredDate
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}