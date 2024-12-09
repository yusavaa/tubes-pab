package com.example.tubespab

import NavigationController
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class InventoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataList: ArrayList<InventoryData>
    lateinit var itemNameList: Array<String>
    lateinit var expiredDateList: Array<String>
    lateinit var stockList: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inventory)

        itemNameList = arrayOf(
            "Fresh Milk",
            "Egg",
            "Fresh Milk",
            "Egg",
            "Fresh Milk",
            "Egg",
            "Fresh Milk",
            "Egg",
            "Fresh Milk",
            "Egg",
        )

        expiredDateList = arrayOf(
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
            "12 December 2024",
        )

        stockList = arrayOf(
            "3",
            "12",
            "3",
            "12",
            "3",
            "12",
            "3",
            "12",
            "3",
            "12",
            "3",
            "12",
        )

        recyclerView = findViewById(R.id.inventoryList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf<InventoryData>()
        getData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.inventory)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        NavigationController.navAction(this)
    }
    private fun getData() {
        for (i in itemNameList.indices) {
            val inventoryData = InventoryData(itemNameList[i], expiredDateList[i], stockList[i])
            dataList.add(inventoryData)
        }
        recyclerView.adapter = InventoryAdapter(dataList)
    }

}