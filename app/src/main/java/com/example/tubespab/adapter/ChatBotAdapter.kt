package com.example.tubespab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.chatbot.Message

class ChatBotAdapter(private var dataList: MutableList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_USER = 0
    private val VIEW_TYPE_BOT = 1

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userMessage: TextView = itemView.findViewById(R.id.tvUserMessage)
    }

    class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val botMessage: TextView = itemView.findViewById(R.id.tvBotMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_USER -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_bubble_chat, parent, false)
                UserViewHolder(itemView)
            }
            VIEW_TYPE_BOT -> {
                val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bot_bubble_chat, parent, false)
                BotViewHolder(itemView)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = dataList[position]

        when (holder) {
            is UserViewHolder -> {
                holder.userMessage.text = currentMessage.content
            }
            is BotViewHolder -> {
                holder.botMessage.text = currentMessage.content
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = dataList[position]
        return if (currentMessage.role == "user") {
            VIEW_TYPE_USER
        } else {
            VIEW_TYPE_BOT
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun addMessage(message: Message) {
        dataList.add(message)
        notifyItemInserted(dataList.size - 1)  // Notify RecyclerView about the new item
    }
}

