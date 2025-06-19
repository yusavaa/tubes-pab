package com.example.tubespab.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubespab.model.Item
import com.example.tubespab.model.ShopItem
import com.example.tubespab.viewmodel.InventoryViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ItemRepository {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("item")

    fun getItems(): LiveData<List<Item>> {
        val liveData = MutableLiveData<List<Item>>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Item>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Item::class.java)
                    item?.let { items.add(it) }
                }
                liveData.value = items
            }
            override fun onCancelled(error: DatabaseError) {
                liveData.value = emptyList()
            }
        })
        return liveData
    }

    fun getItemById(itemId: String): MutableLiveData<Item?> {
        val liveData = MutableLiveData<Item?>()

        myRef.child(itemId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Item::class.java)
                liveData.value = item
            }

            override fun onCancelled(error: DatabaseError) {
                liveData.value = null
            }
        })

        return liveData
    }


    fun removeItem(itemId: String) {
        myRef.child(itemId).removeValue()
    }

    fun updateItem(itemId: String, item: Map<String, Any>) {
        myRef.child(itemId).updateChildren(item)
    }

    fun addItem(item: Item, callback: (String) -> Unit) {
        val itemId = "item" + myRef.push().key
        myRef.child(itemId).setValue(item).addOnSuccessListener {
            callback(itemId)
        }
    }

//    fun getItemForChatBot(inventoryId: String, inventoryViewModel: InventoryViewModel): LiveData<String> {
//        val liveData = MutableLiveData<String>()
//        val itemIdsLiveData = inventoryViewModel.getAllUserItemId(inventoryId)
//        itemIdsLiveData.observeForever { itemIds ->
//            myRef.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    val stringInfor = ""
//                    for (itemSnapshot in snapshot.children)
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
//        }
//    }

    fun getExpiringItems(inventoryId: String, inventoryViewModel: InventoryViewModel): LiveData<List<Item>> {
        val liveData = MutableLiveData<List<Item>>()

        val itemIdsLiveData = inventoryViewModel.getAllUserItemId(inventoryId)
        itemIdsLiveData.observeForever { itemIds ->
            // Mendapatkan referensi ke database
            myRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val expiringItems = mutableListOf<Item>()
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(Item::class.java)
                        item?.let {
                            // Memeriksa apakah item ID ada dalam list itemIds
                            if (itemIds.contains(itemSnapshot.key)) {
                                val daysUntilExpiry = it.getDaysUntilExpiry()
                                if (daysUntilExpiry in 1..7) { // Filter items expiring within 1 to 7 days
                                    expiringItems.add(it)
                                }
                            }
                        }
                    }
                    liveData.value = expiringItems
                }
                override fun onCancelled(error: DatabaseError) {
                    liveData.value = emptyList()
                }
            })
        }
        return liveData
    }

}