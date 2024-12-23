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

    fun getItemByCartId(cartId: String): LiveData<Pair<List<String>, Pair<List<ShopItem>, List<ShopItem>>>> {
        val liveData = MutableLiveData<Pair<List<String>, Pair<List<ShopItem>, List<ShopItem>>>>()

        cartRef.child(cartId).child("cartItem")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemIds = snapshot.children.mapNotNull { it.key }
                    Log.d("Debug", "Item IDs from cart: ${itemIds.size}, $itemIds")

                    val insideCartItems = mutableListOf<ShopItem>()
                    val outsideCartItems = mutableListOf<ShopItem>()

                    for (itemId in itemIds) {
                        val isInCart = snapshot.child(itemId).getValue(Boolean::class.java) ?: false
                        shopItemRef.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(itemSnapshot: DataSnapshot) {
                                val item = itemSnapshot.getValue(ShopItem::class.java)
                                item?.let {
                                    if (isInCart) {
                                        insideCartItems.add(it)
                                        // Log when an item is added to insideCartItems
                                        Log.d("Debug", "Item added to Inside Cart: ${it.name}")
                                    } else {
                                        outsideCartItems.add(it)
                                        // Log when an item is added to outsideCartItems
                                        Log.d("Debug", "Item added to Outside Cart: ${it.name}")
                                    }
                                }

                                // Check if all items are processed and update liveData
                                if (insideCartItems.size + outsideCartItems.size == itemIds.size) {
                                    Log.d("Debug", "Inside Cart: ${insideCartItems.map { it.name }}")
                                    Log.d("Debug", "Outside Cart: ${outsideCartItems.map { it.name }}")
                                    liveData.value = Pair(itemIds, Pair(insideCartItems, outsideCartItems))
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                liveData.value = Pair(emptyList(), Pair(emptyList(), emptyList()))
                            }
                        })
                    }

                    // If no items found, update liveData with empty lists
                    if (itemIds.isEmpty()) {
                        liveData.value = Pair(emptyList(), Pair(emptyList(), emptyList()))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    liveData.value = Pair(emptyList(), Pair(emptyList(), emptyList()))
                }
            })

        return liveData
    }


    fun setCartItemToTrue(userId: String, itemId: String) {
        cartRef.child(userId).child("cartItem").child(itemId).setValue(true)
    }

    fun removeCartItem(cartId: String, shopItemId: String) {
        cartRef.child(cartId).child("cartItem").child(shopItemId).removeValue()
    }

    fun addCartItem(cartId: String, shopItemId: String) {
        val cartItemRef = cartRef.child(cartId).child("cartItem")

        val newCartItem = mapOf(shopItemId to false)

        cartItemRef.updateChildren(newCartItem)
    }
}