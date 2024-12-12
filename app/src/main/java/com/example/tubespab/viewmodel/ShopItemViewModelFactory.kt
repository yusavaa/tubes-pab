package com.example.tubespab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tubespab.repository.ShopItemRepository

class ShopItemViewModelFactory(private val shopItemRepository: ShopItemRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShopItemViewModel::class.java)) {
            return ShopItemViewModel(shopItemRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}