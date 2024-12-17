package com.example.tubespab.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.ShopItem
import com.example.tubespab.viewmodel.CartViewModel
import com.example.tubespab.viewmodel.ShopItemViewModel

class ShoppingAdapter(
    private var currentUserId: String,
    private var itemIds: List<String>,  // Menambahkan itemIds sebagai parameter
    private var dataList: List<ShopItem>, // Menjaga dataList yang berisi ShopItem
    private val cartViewModel: CartViewModel, // Menambahkan cartViewModel sebagai parameter
    private val shopItemViewModel: ShopItemViewModel,
) : RecyclerView.Adapter<ShoppingAdapter.ViewHolderClass>() {

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvUnitType: TextView = itemView.findViewById(R.id.tvUnitType)
        val etQuantity: TextView = itemView.findViewById(R.id.etQuantity)
        val btnTrash: ImageButton = itemView.findViewById(R.id.btnTrash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.shopping_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.tvItemName.text = currentItem.name
        holder.tvUnitType.text = currentItem.unitType
        holder.etQuantity.text = currentItem.quantity.toString()

        val currentItemId = itemIds[position]
        holder.btnTrash.setOnClickListener {
            cartViewModel.removeCartItem(currentUserId, currentItemId)
            shopItemViewModel.removeCartItem(currentItemId)
            Log.d("ItemId", "Item ID to be removed: $currentItemId")
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateData(itemIds: List<String>, newData: List<ShopItem>) {
        this.itemIds = itemIds
        this.dataList = newData
        notifyDataSetChanged()
    }
}
