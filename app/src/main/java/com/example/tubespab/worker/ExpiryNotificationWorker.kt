package com.example.tubespab.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.tubespab.R
import com.example.tubespab.model.Item
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ExpiryNotificationWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        checkExpiringItemsForLoggedInUser()
        return Result.success()
    }

    private fun checkExpiringItemsForLoggedInUser() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val inventoryRef = FirebaseDatabase.getInstance().getReference("inventory")
            inventoryRef.child(userId).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val fridgeItems = snapshot.child("fridgeItem").value as? Map<String, Boolean> ?: emptyMap()
                    val pantryItems = snapshot.child("pantryItem").value as? Map<String, Boolean> ?: emptyMap()

                    val allItems = fridgeItems.keys + pantryItems.keys

                    val itemRef = FirebaseDatabase.getInstance().getReference("item")
                    itemRef.get().addOnSuccessListener { itemSnapshot ->
                        for (itemKey in allItems) {
                            val item = itemSnapshot.child(itemKey).getValue(Item::class.java)
                            item?.let {
                                val expiredDateString = it.expiredDate

                                if (!expiredDateString.isNullOrEmpty()) { // Pastikan string tidak kosong atau null
                                    try {
                                        val expiryDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).parse(expiredDateString)

                                        expiryDate?.let { date ->
                                            val diffDays = ((date.time - Calendar.getInstance().timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

                                            if (diffDays in listOf(7, 3, 1)) { // H-7, H-3, H-1
                                                sendNotification(it.name, diffDays)
                                            }
                                        }
                                    } catch (e: ParseException) {
                                        Log.e("ExpiryNotificationWorker", "Failed to parse date: $expiredDateString", e)
                                    }
                                } else {
                                    Log.e("ExpiryNotificationWorker", "Expired date string is null or empty for item: ${it.name}")
                                }
                            }

                        }
                    }
                }
            }
        } else {
            Log.d("Inventory", "User not logged in")
        }
    }


    private fun sendNotification(itemName: String, daysUntilExpiry: Int) {
        val channelId = "expiry_notifications"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(channelId, "Item Expiry Notifications", NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notif_icon) // Ganti dengan ikon Anda
            .setContentTitle("Item Hampir Kedaluwarsa!")
            .setContentText("Item \"$itemName\" akan kedaluwarsa dalam $daysUntilExpiry hari.")
            .setAutoCancel(true)
            .build()

        notificationManager.notify(itemName.hashCode(), notification)
    }
}
