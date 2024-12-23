package com.example.tubespab.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.adapter.NotificationAdapter
import com.example.tubespab.model.Notification
import com.example.tubespab.repository.InventoryRepository
import com.example.tubespab.repository.ItemRepository
import com.example.tubespab.util.AuthController
import com.example.tubespab.viewmodel.InventoryViewModel
import com.example.tubespab.viewmodel.InventoryViewModelFactory
import com.example.tubespab.viewmodel.ItemViewModel
import com.example.tubespab.viewmodel.ItemViewModelFactory

class NotificationActivity : AppCompatActivity() {

    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory(InventoryRepository())
    }

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory(ItemRepository())
    }

    private val inventoryId = AuthController.getCurrentUserUid()

    private lateinit var recyclerView: RecyclerView
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        recyclerView = findViewById(R.id.notifBox)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Mengamati perubahan data dari ViewModel
        if (inventoryId != null) {
            itemViewModel.getExpiringItems(inventoryId, inventoryViewModel).observe(this) { expiringItems ->
                if (expiringItems.isEmpty()) {
                    Toast.makeText(this, "No items expiring soon", Toast.LENGTH_SHORT).show()
                } else {
                    // Map data item kedaluwarsa ke model notifikasi
                    val notifications = expiringItems.map {
                        Notification(it.name, "Expires in ${it.getDaysUntilExpiry()} days")
                    }

                    // Menampilkan data dalam RecyclerView
                    notificationAdapter = NotificationAdapter(notifications)
                    recyclerView.adapter = notificationAdapter
                }
            }
        }

        // Set up untuk edge to edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
