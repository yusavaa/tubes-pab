package com.example.tubespab.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Item
import com.example.tubespab.repository.InventoryRepository
import com.example.tubespab.repository.ItemRepository
import com.example.tubespab.repository.MissionRepository
import com.example.tubespab.repository.UserRepository
import com.example.tubespab.util.AuthController
import com.example.tubespab.viewmodel.InventoryViewModel
import com.example.tubespab.viewmodel.InventoryViewModelFactory
import com.example.tubespab.viewmodel.ItemViewModel
import com.example.tubespab.viewmodel.ItemViewModelFactory
import com.example.tubespab.viewmodel.MissionViewModel
import com.example.tubespab.viewmodel.MissionViewModelFactory
import com.example.tubespab.viewmodel.UserViewModel
import com.example.tubespab.viewmodel.UserViewModelFactory
import com.google.android.material.button.MaterialButtonToggleGroup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddItemActivity : AppCompatActivity() {

    private val inventoryId = AuthController.getCurrentUserUid()
    private lateinit var btnDate: Button
    private lateinit var tvDate: TextView

    private lateinit var segment: String

    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory(ItemRepository())
    }
    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory(InventoryRepository())
    }
    private val missionViewModel: MissionViewModel by viewModels {
        MissionViewModelFactory(MissionRepository())
    }
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserRepository())
    }

    private var selectedIconRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etItemName: EditText = findViewById(R.id.etItemName)
        val etQuantity: EditText = findViewById(R.id.etQuantity)

        val toggleGroup: MaterialButtonToggleGroup = findViewById(R.id.toggleGroup)
        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnFridge -> {
                        segment = "fridgeItem"
                    }
                    R.id.btnPantry -> {
                        segment = "pantryItem"
                    }
                }
            }
        }
        toggleGroup.check(R.id.btnFridge)

        val sUnitType = findViewById<Spinner>(R.id.unitType)
        ArrayAdapter.createFromResource(
            this, R.array.unit_type, android.R.layout.simple_spinner_item
        ).also {
                choiceList -> choiceList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sUnitType.adapter = choiceList
        }

        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
        tvDate = findViewById(R.id.tvDate)
        tvDate.text = dateFormat.format(currentDate)

        btnDate = findViewById(R.id.btnDate)
        btnDate.setOnClickListener {
            showDatePicker()
        }

        val btnPickIcon: ImageButton = findViewById(R.id.btnPickIcon)
        btnPickIcon.setOnClickListener {
            showIconSelectionDialog()
        }

        val btnAdd: ImageButton = findViewById(R.id.btnAdd)
        btnAdd.setOnClickListener {
            val item = Item(
                etItemName.text.toString(),
                tvDate.text.toString(),
                etQuantity.text.toString().toInt(),
                sUnitType.selectedItem.toString(),
                selectedIconRes.toString(),
            )
            itemViewModel.addItem(item) { itemId ->
                if (inventoryId != null) {
                    inventoryViewModel.addSegmentItem(inventoryId, segment, itemId)
                }
            }
            var progress: Int = 0
            var missionPoint: Int = 0
            var missionExperience: Int = 0
            var point: Int = 0
            var level: Int = 0
            var experience: Int = 0
            missionViewModel.getMissionById("mission1") { mission ->
                if (mission != null) {
                    progress = mission.progress
                    missionPoint = mission.point
                    missionExperience = mission.experience

                    if (progress + 50 < 100) {
                        missionViewModel.updateProgressMission("mission1", progress + 50)
                    } else {
                        missionViewModel.updateProgressMission("mission1", progress + 50)
                        if (inventoryId != null) {
                            userViewModel.getUserById(inventoryId) { user ->
                                if (user != null) {
                                    point = user.point + missionPoint
                                    level = user.level
                                    experience = user.experience
                                }
                                userViewModel.updateUserPoint(inventoryId, point)
                                userViewModel.updateUserXP(inventoryId, missionExperience)
                                if (experience >= 100) {
                                    userViewModel.updateUserLevel(inventoryId, level+1)
                                }
                            }
                        }
                    }
                }
            }
            finish()
        }

        val btnCancel: ImageButton = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            finish()
        }
    }

    private fun showIconSelectionDialog() {
        val icons = arrayOf(
            R.drawable.snack_icon, R.drawable.bottle_icon, R.drawable.fruit_icon,
            R.drawable.ice_cream_icon, R.drawable.vegetable_icon, R.drawable.cake_icon,
            R.drawable.meat_icon, R.drawable.milk_icon, R.drawable.egg_icon
        )

        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val imageView = ImageView(this@AddItemActivity)
                imageView.layoutParams = ViewGroup.LayoutParams(200, 200)
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                return object : RecyclerView.ViewHolder(imageView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val imageView = holder.itemView as ImageView
                imageView.setImageResource(icons[position])

                imageView.setOnClickListener {
                    // Menyimpan resource ID ikon yang dipilih
                    selectedIconRes = icons[position]

                    // Menampilkan ikon yang dipilih di ImageView di layout utama
                    val ivIcon: ImageView = findViewById(R.id.ivIcon)
                    ivIcon.setImageResource(selectedIconRes!!) // default_icon jika null
                }
            }


            override fun getItemCount(): Int = icons.size
        }

        recyclerView.adapter = adapter

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Icon")

        val padding = 16 // Padding in dp
        val density = resources.displayMetrics.density
        val paddingInPx = (padding * density).toInt()

        val container = FrameLayout(this).apply {
            setPadding(paddingInPx, paddingInPx, paddingInPx, paddingInPx) // Set padding to the entire container
            addView(recyclerView)
        }
        builder.setView(container)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)
                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                tvDate.text = formattedDate.toString()
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}