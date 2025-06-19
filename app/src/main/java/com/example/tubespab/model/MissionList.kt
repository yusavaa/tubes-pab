package com.example.tubespab.model

data class MissionList (
    val owner: String,
    val missions: Map<String, Boolean>? = null,
) {
    constructor() : this("", mapOf())
}
