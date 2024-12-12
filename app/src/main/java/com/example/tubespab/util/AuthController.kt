package com.example.tubespab.util

import com.google.firebase.auth.FirebaseAuth

object AuthController {
    fun getCurrentUserUid(): String? {
        val user = FirebaseAuth.getInstance().currentUser
        return user?.uid
    }
}