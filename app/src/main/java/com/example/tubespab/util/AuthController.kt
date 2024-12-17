package com.example.tubespab.util

import android.content.Context
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import com.example.tubespab.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.Locale

object AuthController {
    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun getCurrentUserUid(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun getCurrentUserJoinedDate(): String {
        val user = FirebaseAuth.getInstance().currentUser
        return if (user != null) {
            val creationTimestamp = user.metadata?.creationTimestamp

            if (creationTimestamp != null) {
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                sdf.format(creationTimestamp)
            } else {
                "Creation timestamp not available"
            }
        } else {
            "No user signed in"
        }
    }

    fun signOut(context: Context) {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}