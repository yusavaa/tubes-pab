package com.example.tubespab.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tubespab.repository.MissionRepository

class MissionViewModelFactory(private val missionRepository: MissionRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MissionViewModel::class.java)) {
            return MissionViewModel(missionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}