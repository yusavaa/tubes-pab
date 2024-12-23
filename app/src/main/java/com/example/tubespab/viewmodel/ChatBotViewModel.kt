package com.example.tubespab.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tubespab.repository.ChatBotRepository

class ChatBotViewModel(private val repository: ChatBotRepository) : ViewModel() {

    private val _botResponse = MutableLiveData<String?>()
    val botResponse: MutableLiveData<String?> get() = _botResponse

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> get() = _error

    fun sendMessage(userMessage: String) {
        repository.sendMessage(userMessage).observeForever { response ->
            if (response != null && response.startsWith("Error:")) {
                _error.postValue(response)
            } else {
                _botResponse.postValue(response)
            }
        }
    }
}
