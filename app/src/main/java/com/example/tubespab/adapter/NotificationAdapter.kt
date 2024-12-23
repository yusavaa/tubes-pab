package com.example.tubespab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Notification

class NotificationAdapter(
    private val notificationList: List<Notification>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_card, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.notifTitle.text = notification.title
        holder.notifDesc.text = notification.description
    }

    override fun getItemCount(): Int {
        return notificationList.size
    }

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notifTitle: TextView = itemView.findViewById(R.id.notifTitle)
        val notifDesc: TextView = itemView.findViewById(R.id.notifDesc)
    }
}
