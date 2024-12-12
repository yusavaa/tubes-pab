package com.example.tubespab.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubespab.model.Cart
import com.example.tubespab.model.ShopItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartRepository {
    private val database = FirebaseDatabase.getInstance()
    private val cartRef = database.getReference("cart")
    private val shopItemRef = database.getReference("shopItem")

    fun addUserCart(uidUser: String, email: String) {
        val owner = email.split("@")
        val cart = Cart(
            owner[0]
        )
        cartRef.child(uidUser).setValue(cart)
            .addOnSuccessListener {
                Log.d("AddUserCarty", "Cart berhasil ditambahkan untuk UID: $uidUser")
            }
            .addOnFailureListener { e ->
                Log.e("AddUserCarty", "Gagal menambahkan cart", e)
            }
    }

    fun getItemByCartId(cartId: String): LiveData<List<ShopItem>> {
        val liveData = MutableLiveData<List<ShopItem>>()

        cartRef.child(cartId).child("cartItem")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemIds = snapshot.children.mapNotNull { it.key }
                    val items = mutableListOf<ShopItem>()

                    for (itemId in itemIds) {
                        shopItemRef.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(itemSnapshot: DataSnapshot) {
                                val item = itemSnapshot.getValue(ShopItem::class.java)
                                item?.let { items.add(it) }
                                if (items.size == itemIds.size) {
                                    liveData.value = items
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                liveData.value = emptyList()
                            }
                        })
                    }
                    if (itemIds.isEmpty()) {
                        liveData.value = emptyList()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    liveData.value = emptyList()
                }
            })
        return liveData
    }

    fun addCartItem(cartId: String, shopItemId: String) {
        val cartItemRef = cartRef.child(cartId).child("cartItem")

        val newCartItem = mapOf(shopItemId to false)

        cartItemRef.updateChildren(newCartItem)
    }
}