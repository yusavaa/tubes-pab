package com.example.tubespab.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Item
import com.example.tubespab.ui.EditItemActivity
import com.example.tubespab.viewmodel.InventoryViewModel
import com.example.tubespab.viewmodel.ItemViewModel

class InventoryAdapter(
    private var currentUserId: String,
    private var itemIds: List<String>,
    private var dataList: List<Item>,
    private var inventoryViewModel: InventoryViewModel,
    private var itemViewModel: ItemViewModel,
    private var category: String,
): RecyclerView.Adapter<InventoryAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvExpired: TextView = itemView.findViewById(R.id.tvExpired)
        val tvStock: TextView = itemView.findViewById(R.id.tvStock)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
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
        currentItem.icon?.let {
            val iconRes = it.toIntOrNull() ?: R.drawable.stat_view
            holder.ivIcon.setImageResource(iconRes)
        }
        holder.tvItemName.text = currentItem.name
        holder.tvExpired.text = currentItem.expiredDate
        holder.tvStock.text = currentItem.stock.toString() + " " + currentItem.unitType.toString()

        val currentItemId = itemIds[position]
        holder.btnEdit.setOnClickListener {
//            inventoryViewModel.removeInventoryItem(currentUserId, category, currentItemId)
//            itemViewModel.removeItem(currentItemId)
//            Log.d("ItemId", "Item ID to be removed: $currentItemId")
            val intent = Intent(holder.itemView.context, EditItemActivity::class.java)
            intent.putExtra("itemId", currentItemId)
            intent.putExtra("category", category)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(itemIds: List<String>, newData: List<Item>, category: String) {
        this.itemIds = itemIds
        this.dataList = newData
        this.category = category
        notifyDataSetChanged()
    }
}