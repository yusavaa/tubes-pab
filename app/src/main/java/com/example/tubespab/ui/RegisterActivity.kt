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
import com.example.tubespab.repository.CartRepository
import com.example.tubespab.repository.InventoryRepository
import com.example.tubespab.repository.UserRepository
import com.example.tubespab.viewmodel.CartViewModel
import com.example.tubespab.viewmodel.CartViewModelFactory
import com.example.tubespab.viewmodel.InventoryViewModel
import com.example.tubespab.viewmodel.InventoryViewModelFactory
import com.example.tubespab.viewmodel.UserViewModel
import com.example.tubespab.viewmodel.UserViewModelFactory

class RegisterActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }
    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory(InventoryRepository())
    }
    private val cartViewModel: CartViewModel by viewModels {
        CartViewModelFactory(CartRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etEmail: EditText = findViewById(R.id.etEmail)
        val etFullName: EditText = findViewById(R.id.etFullName)
        val etUsername: EditText = findViewById(R.id.etUsername)
        val etPassword: EditText = findViewById(R.id.etPassword)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val username = etUsername.text.toString()
            val fullName = etFullName.text.toString()

            if (email.isNotBlank() && password.isNotBlank() && username.isNotBlank() && fullName.isNotBlank()) {
                userViewModel.createAccount(email, password, username, fullName) { isSuccess, result, owner ->
                    if (isSuccess) {
                        Toast.makeText(this, "Account created successfully! $result", Toast.LENGTH_SHORT).show()
                        if (owner != null) {
                            inventoryViewModel.addUserInventory(result, owner)
                            cartViewModel.addUserCart(result, owner)
                        }
                    } else {
                        Toast.makeText(this, result, Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            }
        }

        val btnToLogin: Button = findViewById(R.id.btnToLogin)
        btnToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}