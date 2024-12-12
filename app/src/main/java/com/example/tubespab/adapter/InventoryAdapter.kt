package com.example.tubespab.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Item

class InventoryAdapter(private var dataList: List<Item>): RecyclerView.Adapter<InventoryAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvExpired: TextView = itemView.findViewById(R.id.tvExpired)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.inventory_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.tvItemName.text = currentItem.name
        holder.tvExpired.text = currentItem.expiredDate
        holder.tvStock.text = currentItem.stock.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(newData: List<Item>) {
        dataList = newData
        notifyDataSetChanged()
    }
}