package com.example.tubespab.service

import com.example.tubespab.model.chatbot.GroqResponse
import com.example.tubespab.model.chatbot.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface GroqApiService {
    @POST("openai/v1/chat/completions")
    fun sendRequest(@Body requestBody: RequestBody): Call<GroqResponse>
}