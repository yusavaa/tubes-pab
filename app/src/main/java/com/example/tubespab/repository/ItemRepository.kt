package com.example.tubespab.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubespab.model.Item
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

    fun addItem(item: Item) {
        val itemId = myRef.push().key
        if (itemId != null) {
            myRef.child(itemId).setValue(item)
        }
    }
}