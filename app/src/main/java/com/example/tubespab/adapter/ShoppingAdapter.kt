package com.example.tubespab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.ShopItem

class ShoppingAdapter(private var dataList: List<ShopItem>): RecyclerView.Adapter<ShoppingAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvUnitType: TextView = itemView.findViewById(R.id.tvUnitType)
        val etQuantity: TextView = itemView.findViewById(R.id.etQuantity)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shopping_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.tvItemName.text = currentItem.name
        holder.tvUnitType.text = currentItem.unitType
        holder.etQuantity.text = currentItem.quantity.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(newData: List<ShopItem>) {
        dataList = newData
        notifyDataSetChanged()
    }
}