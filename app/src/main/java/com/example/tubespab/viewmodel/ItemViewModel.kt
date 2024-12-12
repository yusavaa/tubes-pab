package com.example.tubespab.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tubespab.model.Item
import com.example.tubespab.repository.ItemRepository

class ItemViewModel(private val itemRepository: ItemRepository) : ViewModel() {
    fun addItem(item: Item) {
        itemRepository.addItem(item)
    }
}