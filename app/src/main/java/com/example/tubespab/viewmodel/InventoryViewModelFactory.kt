package com.example.tubespab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tubespab.repository.InventoryRepository

class InventoryViewModelFactory(private val inventoryRepository: InventoryRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            return InventoryViewModel(inventoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}