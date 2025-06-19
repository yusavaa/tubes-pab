package com.example.tubespab.model

data class Inventory (
    val owner: String,
    val fridgeItem: Map<String, Boolean>? = null,
    val pantryItem: Map<String, Boolean>? = null
) {
    constructor() : this("", mapOf(), mapOf())
}

//wee do wee