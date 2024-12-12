package com.example.tubespab.model

data class Item(
    val name: String,
    val expiredDate: String? = "no date",
    val stock: Int = 0,
    val unitType: String? = null,
    val icon: String? = null,
) {
    constructor() : this("", "", 0)
}
