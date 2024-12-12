package com.example.tubespab.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tubespab.model.ShopItem
import com.example.tubespab.repository.ShopItemRepository

class ShopItemViewModel(private val shopItemRepository: ShopItemRepository) : ViewModel() {
    fun addShopItem(shopItem: ShopItem, callback: (String) -> Unit) {
        shopItemRepository.addShopItem(shopItem, callback)
    }
}