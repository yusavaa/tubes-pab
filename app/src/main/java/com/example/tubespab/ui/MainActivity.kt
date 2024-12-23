package com.example.tubespab.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.tubespab.R
import com.example.tubespab.util.AuthController
import com.example.tubespab.util.NavbarController
import com.example.tubespab.worker.ExpiryNotificationWorker
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit

/*
    Anggota:
    22523173 - Fath Yusava Arden
    22523295 - Alivah Syiva Dewi
    22523119 - Sekar Kinasih
    22523155 - Hasan Abdullah
 */

class MainActivity : AppCompatActivity() {
    private val userId = AuthController.getCurrentUserUid()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
        scheduleNotificationWorker()

        NavbarController.navAction(this)
    }

    private fun scheduleNotificationWorker() {
        val workRequest = PeriodicWorkRequestBuilder<ExpiryNotificationWorker>(1, TimeUnit.DAYS) // Jalankan setiap hari
            .build()

        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
