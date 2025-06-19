package com.example.tubespab.ui

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.adapter.HomeAdapter
import com.example.tubespab.adapter.NotificationAdapter
import com.example.tubespab.model.Item
import com.example.tubespab.model.Notification
import com.example.tubespab.repository.InventoryRepository
import com.example.tubespab.repository.ItemRepository
import com.example.tubespab.repository.UserRepository
import com.example.tubespab.util.AuthController
import com.example.tubespab.viewmodel.InventoryViewModel
import com.example.tubespab.viewmodel.InventoryViewModelFactory
import com.example.tubespab.viewmodel.ItemViewModel
import com.example.tubespab.viewmodel.ItemViewModelFactory
import com.example.tubespab.viewmodel.UserViewModel
import com.example.tubespab.viewmodel.UserViewModelFactory

class HomeFragment : Fragment() {

    private val inventoryId = AuthController.getCurrentUserUid()
    private lateinit var recyclerView: RecyclerView
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var levelBar: ProgressBar

    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory(InventoryRepository())
    }

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory(ItemRepository())
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnNotif: ImageButton = view.findViewById(R.id.btnNotif)
        btnNotif.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        levelBar = view.findViewById(R.id.levelBar)
        levelBar.max = 100

        val tvHello: TextView = view.findViewById(R.id.tvHello)
        val tvPoint: TextView = view.findViewById(R.id.tvPoint)
        val tvLevel: TextView = view.findViewById(R.id.tvLevel)
        if (inventoryId != null) {
            userViewModel.getUserById(inventoryId) { user ->
                if (user != null) {
                    val fullname = user.fullname
                    val firstName = fullname.split(" ")[0]
                    tvHello.text = "Hello, $firstName"
                    tvLevel.text = "Level ${user.level}"
                    levelBar.progress = user.experience
                    tvPoint.text = user.point.toString()
                }
            }
        }

        val btnToChatBot: Button = view.findViewById(R.id.btnToChatBot)
        btnToChatBot.setOnClickListener {
            val intent = Intent(requireContext(), ChatBotActivity::class.java)
            startActivity(intent)
        }
        
        val btnToMission: Button = view.findViewById(R.id.btnToMission)
        btnToMission.setOnClickListener {
            val fragment = MissionFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView = view.findViewById(R.id.expiredBox)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)

        if (inventoryId != null) {
            itemViewModel.getExpiringItems(inventoryId, inventoryViewModel).observe(viewLifecycleOwner) { expiringItems ->
                if (expiringItems.isEmpty()) {
//                    Toast.makeText(this, "No items expiring soon", Toast.LENGTH_SHORT).show()
                } else {
                    val notifications = expiringItems.map {
                        Item(it.name, it.expiredDate, it.stock, it.unitType, it.icon)
                    }
                    homeAdapter = HomeAdapter(notifications)
                    recyclerView.adapter = homeAdapter
                }
            }
        }
    }
}