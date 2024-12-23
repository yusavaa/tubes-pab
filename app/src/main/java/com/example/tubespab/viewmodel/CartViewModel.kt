package com.example.tubespab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tubespab.model.ShopItem
import com.example.tubespab.repository.CartRepository

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {
    fun  addUserCart(uidUser: String, email: String) {
        cartRepository.addUserCart(uidUser, email)
    }

    fun getItemByCartId(cartId: String): LiveData<Pair<List<String>, Pair<List<ShopItem>, List<ShopItem>>>> {
        return cartRepository.getItemByCartId(cartId)
    }

    fun removeCartItem(cartId: String, shopItemId: String) {
        cartRepository.removeCartItem(cartId, shopItemId)
    }

    fun setCartItemToTrue(userId: String, itemId: String) {
        cartRepository.setCartItemToTrue(userId, itemId)
    }

    fun removeCartItemsWithTrueValue(userId: String) {
        cartRepository.removeCartItemsWithTrueValue(userId)
    }

    fun addCartItem(cartId: String, shopItemId: String) {
        cartRepository.addCartItem(cartId, shopItemId)
    }
}