package com.example.tubespab.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tubespab.model.User
import com.example.tubespab.repository.UserRepository

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    val loginStatus = userRepository.loginStatus

    fun createAccount(email: String, password: String, username: String, fullName: String, onResult: (Boolean, String, String?) -> Unit) {
        userRepository.createAccount(email, password, username, fullName, onResult)
    }

    fun loginAccount(email: String, password: String) {
        return userRepository.loginAccount(email, password)
    }

    fun getUserById(userId: String, callback: (User?) -> Unit) {
        return userRepository.getUserById(userId, callback)
    }
}