package com.example.tubespab.model

data class ShopItem(
    val name: String,
    val quantity: Int = 0,
    val unitType: String,
) {
    constructor() : this("", 0, "")
}
