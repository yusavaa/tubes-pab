package com.example.tubespab.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubespab.model.Inventory
import com.example.tubespab.model.Item
import com.example.tubespab.model.ShopItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class InventoryRepository {
    private val database = FirebaseDatabase.getInstance()
    private val inventoryRef = database.getReference("inventory")
    private val itemRef = database.getReference("item")

    fun addUserInventory(uidUser: String, email: String) {
        val owner = email.split("@")
        val inventory = Inventory(
            owner[0]
        )
        inventoryRef.child(uidUser).setValue(inventory)
            .addOnSuccessListener {
                Log.d("AddUserInventory", "Inventory berhasil ditambahkan untuk UID: $uidUser")
            }
            .addOnFailureListener { e ->
                Log.e("AddUserInventory", "Gagal menambahkan inventory", e)
            }
    }

    fun getAllUserItemId(inventoryId: String): LiveData<List<String>> {
        val liveData = MutableLiveData<List<String>>()
        val itemIds = mutableListOf<String>()

        val fridgeRef = inventoryRef.child(inventoryId).child("fridgeItem")
        val pantryRef = inventoryRef.child(inventoryId).child("pantryItem")

        fridgeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    itemIds.addAll(snapshot.children.mapNotNull { it.key })
                }
                pantryRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            itemIds.addAll(snapshot.children.mapNotNull { it.key })
                        }
                        liveData.value = itemIds
                    }
                    override fun onCancelled(error: DatabaseError) {
                        liveData.value = emptyList()
                    }
                })
            }
            override fun onCancelled(error: DatabaseError) {
                liveData.value = emptyList()
            }
        })
        return liveData
    }


    fun getSegmentItems(inventoryId: String, segment: String): LiveData<Pair<List<String>, List<Item>>> {
        val liveData = MutableLiveData<Pair<List<String>, List<Item>>>()

        inventoryRef.child(inventoryId).child(segment)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Debug", "cartRef exists for cartId: $inventoryId = ${snapshot.exists()}")
                    val itemIds = snapshot.children.mapNotNull { it.key }
                    Log.d("Debug", "Item IDs from inventory: ${itemIds}")
                    val items = mutableListOf<Item>()

                    for (itemId in itemIds) {
                        itemRef.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(itemSnapshot: DataSnapshot) {
                                val item = itemSnapshot.getValue(Item::class.java)
                                item?.let { items.add(it) }
                                if (items.size == itemIds.size) {
                                    liveData.value = Pair(itemIds, items)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                liveData.value = Pair(emptyList(), emptyList())
                            }
                        })
                    }
                    if (itemIds.isEmpty()) {
                        liveData.value = Pair(emptyList(), emptyList())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    liveData.value = Pair(emptyList(), emptyList())
                }
            })
        return liveData
    }

    fun removeInventoryItem(cartId: String, segment: String, itemId: String) {
        inventoryRef.child(cartId).child(segment).child(itemId).removeValue()
    }

    fun addSegmentItem(inventoryId: String, segment: String, itemId: String) {
        val inventoryItem = inventoryRef.child(inventoryId).child(segment)

        val newInventoryItem = mapOf(itemId to false)

        inventoryItem.updateChildren(newInventoryItem)
    }
}