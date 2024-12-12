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

    fun getSegmentItems(inventoryId: String, segment: String): LiveData<List<Item>> {
        return inventoryRepository.getSegmentItems(inventoryId, segment)
    }
}