package com.example.tubespab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tubespab.model.Item
import com.example.tubespab.repository.ItemRepository

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    fun getItemById(itemId: String): LiveData<Item?> {
        return  itemRepository.getItemById(itemId)
    }

    fun removeItem(itemId: String) {
        itemRepository.removeItem(itemId)
    }

    fun updateItem(itemId: String, item: Map<String, Any>) {
        itemRepository.updateItem(itemId, item)
    }

    fun getExpiringItems(inventoryId: String, inventoryViewModel: InventoryViewModel): LiveData<List<Item>> {
        return itemRepository.getExpiringItems(inventoryId, inventoryViewModel)
    }

    fun addItem(item: Item, callback: (String) -> Unit) {
        itemRepository.addItem(item, callback)
    }
}