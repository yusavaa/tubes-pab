package com.example.tubespab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tubespab.model.ShopItem
import com.example.tubespab.repository.CartRepository

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    fun  addUserCart(uidUser: String, email: String) {
        cartRepository.addUserCart(uidUser, email)
    }

    fun getItemByCartId(cartId: String): LiveData<List<ShopItem>> {
        return cartRepository.getItemByCartId(cartId)
    }

    fun addCartItem(cartId: String, shopItemId: String) {
        cartRepository.addCartItem(cartId, shopItemId)
    }
}