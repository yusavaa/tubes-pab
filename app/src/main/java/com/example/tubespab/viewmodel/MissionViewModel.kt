package com.example.tubespab.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.tubespab.model.Mission
import com.example.tubespab.repository.MissionRepository

class MissionViewModel(private val missionRepository: MissionRepository) : ViewModel() {
    fun getMissions(): LiveData<List<Mission>> {
        return missionRepository.getMissions()
    }

    fun getMissionById(missionId: String, callback: (Mission?) -> Unit) {
        missionRepository.getMissionById(missionId, callback)
    }

    fun updateProgressMission(missionId: String, newProgress: Int) {
        missionRepository.updateProgressMission(missionId, newProgress)
    }
}