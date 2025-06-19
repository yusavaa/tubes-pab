package com.example.tubespab.model

data class Mission(
    val title: String,
    val point: Int,
    val progress: Int,
    val experience: Int,
    val description: String,
    val icon: String,
) {
    constructor() : this("", 25, 0, 0, "", "")
}