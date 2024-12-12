package com.example.tubespab.model

data class Cart(
    val owner: String,
    val cartItem: Map<String, Boolean>? = null,
) {
    constructor() : this("", mapOf())
}
