package com.example.tubespab.ui

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tubespab.R
import com.example.tubespab.model.chatbot.Message
import com.example.tubespab.repository.ChatBotRepository
import com.example.tubespab.viewmodel.ChatBotViewModel
import com.example.tubespab.viewmodel.ChatBotViewModelFactory
import com.example.tubespab.adapter.ChatBotAdapter

class ChatBotActivity : AppCompatActivity() {

    private val viewModel: ChatBotViewModel by viewModels {
        ChatBotViewModelFactory(ChatBotRepository())
    }
    private val adapter = ChatBotAdapter(mutableListOf())

    private lateinit var chatBox: androidx.recyclerview.widget.RecyclerView
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat_bot)

        chatBox = findViewById(R.id.chatBox)
        etMessage = findViewById(R.id.etMessage)
        btnSend = findViewById(R.id.btnSend)

        chatBox.layoutManager = LinearLayoutManager(this)
        chatBox.adapter = adapter

        btnSend.setOnClickListener {
            val userMessage = etMessage.text.toString()
            if (userMessage.isNotEmpty()) {
                val userChatMessage = Message("user", userMessage)
                adapter.addMessage(userChatMessage) // Show user message

                etMessage.text.clear()

                viewModel.sendMessage(userMessage)
            }
        }

        viewModel.botResponse.observe(this) { response ->
            val botChatMessage = response?.let { Message("assistant", it) }
            if (botChatMessage != null) {
                adapter.addMessage(botChatMessage)
            }
        }

        viewModel.error.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
