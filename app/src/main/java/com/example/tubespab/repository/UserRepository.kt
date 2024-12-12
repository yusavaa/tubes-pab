package com.example.tubespab.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val database = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val myRef = database.getReference("user")

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    fun createAccount(
        email: String,
        password: String,
        username: String,
        fullName: String,
        onResult: (Boolean, String, String?) -> Unit // Boolean untuk status, String? untuk UID atau pesan error
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid
                    val owner = auth.currentUser?.email
                    val user = mapOf(
                        "email" to email,
                        "username" to username,
                        "fullname" to fullName,
                        "experience" to 0,
                        "level" to 0,
                        "point" to 0,
                    )
                    uid?.let {
                        myRef.child(it).setValue(user)
                            .addOnSuccessListener {
                                Log.d("Register", "Data pengguna berhasil disimpan!")
                                onResult(true, uid, owner) // UID berhasil dikembalikan
                            }
                            .addOnFailureListener { e ->
                                Log.e("Register", "Gagal menyimpan data pengguna", e)
                                onResult(false, "Failed to save user data: ${e.message}", null)
                            }
                    } ?: run {
                        onResult(false, "UID is null", null)
                    }
                } else {
                    Log.e("Register", "Gagal mendaftarkan pengguna: ${task.exception?.message}")
                    onResult(false, "Registration failed: ${task.exception?.message}", null)
                }
            }
    }



    fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("Login", "berhasil login ${user?.email}")
                    _loginStatus.value = true
                } else {
                    Log.d("Login", "gagal login")
                    _loginStatus.value = false
                }
            }
    }
}