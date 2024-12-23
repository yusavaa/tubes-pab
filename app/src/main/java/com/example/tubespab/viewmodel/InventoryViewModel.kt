package com.example.tubespab.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tubespab.model.Item
import com.example.tubespab.repository.InventoryRepository

class InventoryViewModel(private val inventoryRepository: InventoryRepository) : ViewModel() {
    fun  addUserInventory(uidUser: String, email: String) {
        inventoryRepository.addUserInventory(uidUser, email)
    }

    fun getSegmentItems(inventoryId: String, segment: String): LiveData<Pair<List<String>, List<Item>>> {
        return inventoryRepository.getSegmentItems(inventoryId, segment)
    }

    fun getAllUserItemId(inventoryId: String): LiveData<List<String>> {
        return inventoryRepository.getAllUserItemId(inventoryId)
    }

    fun removeInventoryItem(cartId: String, segment: String, itemId: String) {
        inventoryRepository.removeInventoryItem(cartId, segment, itemId)
    }

    fun addSegmentItem(inventoryId: String, segment: String, itemId: String) {
        inventoryRepository.addSegmentItem(inventoryId, segment, itemId)
    }
}