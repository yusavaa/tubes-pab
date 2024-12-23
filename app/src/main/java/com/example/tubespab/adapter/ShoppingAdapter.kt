package com.example.tubespab.adapter

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
    private var itemIds: List<String>,
    private var insideCartItems: List<ShopItem>,
    private var outsideCartItems: List<ShopItem>,
    private val cartViewModel: CartViewModel,
    private val shopItemViewModel: ShopItemViewModel,
) : RecyclerView.Adapter<ShoppingAdapter.ShoppingItemViewHolder>() {

    // ViewHolder untuk Shopping Item
    class ShoppingItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvItemName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvUnitType: TextView = itemView.findViewById(R.id.tvUnitType)
        val etQuantity: TextView = itemView.findViewById(R.id.etQuantity)
        val btnCart: ImageButton = itemView.findViewById(R.id.btnCart)
        val btnTrash: ImageButton = itemView.findViewById(R.id.btnTrash)
        val btnPlus: ImageButton = itemView.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = itemView.findViewById(R.id.btnMinus)
    }

    // onCreateViewHolder untuk Shopping Item dengan pengecekan layout berdasarkan kategori
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val itemView = if (viewType == 0) {
            // Layout untuk Outside Cart
            LayoutInflater.from(parent.context).inflate(R.layout.shopping_card, parent, false)
        } else {
            // Layout untuk Inside Cart
            LayoutInflater.from(parent.context).inflate(R.layout.shopping_card_true, parent, false)
        }
        return ShoppingItemViewHolder(itemView)
    }

    // onBindViewHolder untuk Shopping Item
    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        // Menentukan apakah item berada dalam outsideCart atau insideCart
        val actualPosition = if (position < outsideCartItems.size) {
            position // Untuk items di "Outside Cart"
        } else {
            position - outsideCartItems.size // Untuk items di "Inside Cart"
        }

        val currentItem = if (position < outsideCartItems.size) {
            outsideCartItems[actualPosition]
        } else {
            insideCartItems[actualPosition]
        }

        val currentItemId = itemIds[position]

        // Mengisi informasi item
        holder.tvItemName.text = currentItem.name
        holder.tvUnitType.text = currentItem.unitType
        holder.etQuantity.text = currentItem.quantity.toString()

        // Handle tombol aksi
        holder.btnCart.setOnClickListener {
            cartViewModel.setCartItemToTrue(currentUserId, currentItemId)
        }

        holder.btnTrash.setOnClickListener {
            cartViewModel.removeCartItem(currentUserId, currentItemId)
            shopItemViewModel.removeCartItem(currentItemId)
        }

        holder.btnPlus.setOnClickListener {
            val currentQuantity = currentItem.quantity
            val newQuantity = currentQuantity + 1
            currentItem.quantity = newQuantity
            holder.etQuantity.text = newQuantity.toString()
        }

        holder.btnMinus.setOnClickListener {
            val currentQuantity = currentItem.quantity
            if (currentQuantity > 1) {
                val newQuantity = currentQuantity - 1
                currentItem.quantity = newQuantity
                holder.etQuantity.text = newQuantity.toString()
            }
        }
    }

    // Menentukan tipe layout berdasarkan kategori item
    override fun getItemViewType(position: Int): Int {
        return if (position < outsideCartItems.size) {
            0 // Outside Cart: layout shopping_card.xml
        } else {
            1 // Inside Cart: layout shopping_card_true.xml
        }
    }

    // Mengembalikan jumlah item (tanpa Cart Title)
    override fun getItemCount(): Int {
        return insideCartItems.size + outsideCartItems.size
    }

    // Update data pada adapter
    fun updateData(newItemIds: List<String>, insideCartItems: List<ShopItem>, outsideCartItems: List<ShopItem>) {
        this.itemIds = newItemIds
        this.insideCartItems = insideCartItems
        this.outsideCartItems = outsideCartItems
        notifyDataSetChanged()
    }
}
