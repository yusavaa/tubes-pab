package com.example.tubespab.model.chatbot

data class RequestBody(
    val messages: List<Message>,
    val model: String,
    val temperature: Double,
    val max_tokens: Int,
    val top_p: Double
)
