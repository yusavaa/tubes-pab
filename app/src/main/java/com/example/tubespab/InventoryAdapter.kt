package com.example.tubespab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InventoryAdapter (private val dataList: ArrayList<InventoryData>): RecyclerView.Adapter<InventoryAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvExpired: TextView = itemView.findViewById(R.id.tvExpired)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InventoryAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inventory_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: InventoryAdapter.ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.tvItemName.text = currentItem.itemName
        holder.tvExpired.text = currentItem.expiredDate
        holder.tvStock.text = currentItem.stock
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}