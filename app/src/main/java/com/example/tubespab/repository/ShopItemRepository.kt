package com.example.tubespab.repository

import com.example.tubespab.model.ShopItem
import com.google.firebase.database.FirebaseDatabase

class ShopItemRepository {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("shopItem")

    fun addShopItem(shopItem: ShopItem, callback: (String) -> Unit) {
        val shopItemId = "shopItem" + myRef.push().key
        myRef.child(shopItemId).setValue(shopItem).addOnSuccessListener {
            callback(shopItemId)
        }
    }
}