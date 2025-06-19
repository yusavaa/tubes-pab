package com.example.tubespab.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Mission

class MissionAdapter(
    private var dataList: List<Mission>
): RecyclerView.Adapter<MissionAdapter.ViewHolderClass>() {
    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val pbMissionBar: ProgressBar = itemView.findViewById(R.id.missionBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionAdapter.ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mission_card, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.tvTitle.text = currentItem.title
        holder.tvDescription.text = currentItem.description
        currentItem.icon.let {
            val iconRes = it.toIntOrNull() ?: R.drawable.stat_view
            holder.ivIcon.setImageResource(iconRes)
        }
        holder.pbMissionBar.max = 100
        holder.pbMissionBar.progress = currentItem.progress
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateMissions(dataList: List<Mission>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }
}