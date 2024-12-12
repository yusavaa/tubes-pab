package com.example.tubespab.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.tubespab.R
import com.example.tubespab.repository.UserRepository
import com.example.tubespab.viewmodel.UserViewModel
import com.example.tubespab.viewmodel.UserViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        userViewModel.loginStatus.observe(this) { isLogin ->
            if (isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                Toast.makeText(this, "Login gagal, periksa email dan password", Toast.LENGTH_SHORT).show()
            }
        }

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPassword: EditText = findViewById(R.id.etPassword)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            userViewModel.loginAccount(
                etEmail.text.toString(),
                etPassword.text.toString(),
            )
        }
    }
}