package com.example.tubespab.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tubespab.model.Inventory
import com.example.tubespab.model.Mission
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MissionRepository {
    private val database = FirebaseDatabase.getInstance()
    private val myRef = database.getReference("mission")

    fun addUserInventory(uidUser: String, email: String) {
        val owner = email.split("@")
        val inventory = Inventory(
            owner[0]
        )
        myRef.child(uidUser).setValue(inventory)
            .addOnSuccessListener {
                Log.d("AddUserInventory", "Inventory berhasil ditambahkan untuk UID: $uidUser")
            }
            .addOnFailureListener { e ->
                Log.e("AddUserInventory", "Gagal menambahkan inventory", e)
            }
    }

    fun getMissions(): LiveData<List<Mission>> {
        val liveData = MutableLiveData<List<Mission>>()

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val items = mutableListOf<Mission>()
                for (itemSnapshot in snapshot.children) {
                    val item = itemSnapshot.getValue(Mission::class.java)
                    item?.let { items.add(it) }
                }
                liveData.value = items
            }
            override fun onCancelled(error: DatabaseError) {
                liveData.value = emptyList()
            }
        })
        return liveData
    }

    fun getMissionById(missionId: String, callback: (Mission?) -> Unit) {
        myRef.child(missionId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val mission = snapshot.getValue(Mission::class.java)
                        callback(mission)
                    } else {
                        callback(null)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }

    fun updateProgressMission(missionId: String, newProgress: Int) {
        val missionRef = myRef.child(missionId).child("progress")
        missionRef.setValue(newProgress)
    }
}