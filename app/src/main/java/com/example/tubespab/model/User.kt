package com.example.tubespab.model

data class User(
    val email: String,
    val experience: Int = 0,
    val fullname: String,
    val level: Int = 0,
    val password: String,
    val point: Int = 0,
    val username: String,
) {
    constructor() : this("", 0, "", 0, "", 0, "")
}