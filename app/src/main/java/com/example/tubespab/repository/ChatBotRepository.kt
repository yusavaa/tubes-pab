package com.example.tubespab.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tubespab.model.chatbot.GroqResponse
import com.example.tubespab.model.chatbot.Message
import com.example.tubespab.model.chatbot.RequestBody
import com.example.tubespab.util.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChatBotRepository {

    private val model = "llama3-8b-8192"
    private val temperature = 1.0
    private val maxTokens = 1024
    private val topP = 1.0

    fun sendMessage(userMessage: String): MutableLiveData<String?> {

        val liveData = MutableLiveData<String?>()
        val context = "Provide simple answers only to questions related to food waste, cooking, recipes, and similar topics. If the question falls outside these areas, politely and kindly decline in either English or Indonesian."

        Log.d("ChatBotRepository", "Sending user message: $userMessage")

        val messageList = listOf(
            Message(role = "system", content = context),
            Message(role = "user", content = userMessage)
        )

        val requestBody = RequestBody(
            messages = messageList,
            model = model,
            temperature = temperature,
            max_tokens = maxTokens,
            top_p = topP
        )

        RetrofitClient.instance.sendRequest(requestBody).enqueue(object : Callback<GroqResponse> {
            override fun onResponse(call: Call<GroqResponse>, response: Response<GroqResponse>) {
                if (response.isSuccessful) {
                    val messageContent = response.body()?.choices?.firstOrNull()?.message?.content

                    if (messageContent != null) {
                        Log.d("ChatBotRepository", "Received bot response: $messageContent")
                        liveData.value = messageContent // Success response
                    } else {
                        val errorMessage = "No result found in response."
                        Log.e("ChatBotRepository", errorMessage)
                        liveData.value = errorMessage // Error message
                    }
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("ChatBotRepository", "Error: ${response.code()} - $errorMessage") // API error
                    liveData.value = "Error: ${response.code()} - $errorMessage" // API error
                }
            }

            override fun onFailure(call: Call<GroqResponse>, t: Throwable) {
                Log.e("ChatBotRepository", "Request failed: ${t.message}")
                liveData.value = "Request failed: ${t.message}" // Network failure
            }
        })

        return liveData
    }
}
