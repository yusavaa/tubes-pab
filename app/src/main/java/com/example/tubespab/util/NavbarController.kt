package com.example.tubespab.util

import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tubespab.ui.InventoryFragment
import com.example.tubespab.R
import com.example.tubespab.ui.HomeFragment
import com.example.tubespab.ui.ShoppingFragment

object NavbarController {
    private fun handleNavigation(activity: AppCompatActivity, buttonId: String) {
        when (buttonId) {
            "home" -> {
                val fragment = HomeFragment()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            "inventory" -> {
                val fragment = InventoryFragment()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            "shopping" -> {
                val fragment = ShoppingFragment()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    fun navAction(activity: AppCompatActivity) {
        val buttonHome: Button = activity.findViewById(R.id.homeButton)
        val buttonInventory: Button = activity.findViewById(R.id.inventoryButton)
        val buttonShopping: Button = activity.findViewById(R.id.shoppingButton)

        buttonHome.setOnClickListener {
            handleNavigation(activity, "home")
        }

        buttonInventory.setOnClickListener {
            handleNavigation(activity, "inventory")
        }

        buttonShopping.setOnClickListener {
            handleNavigation(activity, "shopping")
        }
    }
}