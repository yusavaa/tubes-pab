package com.example.tubespab.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
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
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.model.Item
import com.example.tubespab.repository.ItemRepository
import com.example.tubespab.viewmodel.ItemViewModel
import com.example.tubespab.viewmodel.ItemViewModelFactory
import com.google.android.material.button.MaterialButtonToggleGroup
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditItemActivity : AppCompatActivity() {

    private lateinit var btnDate: Button
    private lateinit var tvDate: TextView
    private lateinit var segment: String
    private val itemViewModel: ItemViewModel by viewModels {
        ItemViewModelFactory(ItemRepository())
    }

    // UI elements
    private lateinit var ivIcon: ImageView
    private lateinit var etItemName: EditText
    private lateinit var etQuantity: EditText
    private lateinit var toggleGroup: MaterialButtonToggleGroup
    private lateinit var sUnitType: Spinner
    private lateinit var btnCancel: ImageButton
    private lateinit var btnAdd: ImageButton

    // Declare itemId here
    private var itemId: String? = null

    private var selectedIconRes: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()  // Function to initialize the UI components
        observeItemData()  // Observe the LiveData for item details
        setupDatePicker()  // Set up the date picker dialog
    }

    // Function to setup the UI components
    private fun setupUI() {
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_item)
        applyWindowInsets()

        ivIcon = findViewById(R.id.ivIcon)
        etItemName = findViewById(R.id.etItemName)
        etQuantity = findViewById(R.id.etQuantity)
        tvDate = findViewById(R.id.tvDate)
        btnDate = findViewById(R.id.btnDate)
        toggleGroup = findViewById(R.id.toggleGroup)
        sUnitType = findViewById(R.id.unitType)
        btnCancel = findViewById(R.id.btnCancel)
        btnAdd = findViewById(R.id.btnAdd)

        // Setting up the unit type spinner
        setupUnitTypeSpinner()

        // Getting category and itemId from Intent
        itemId = intent.getStringExtra("itemId")
        val category = intent.getStringExtra("category") ?: "fridgeItem" // Default to "fridgeItem" if category is null

        // Set default value for the toggle button based on the category
        setDefaultToggle(category)

        val btnPickIcon: ImageButton = findViewById(R.id.btnPickIcon)
        btnPickIcon.setOnClickListener {
            showIconSelectionDialog()
        }

        // Setting up the toggle buttons for Fridge and Pantry
        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                segment = when (checkedId) {
                    R.id.btnFridge -> "fridgeItem"
                    R.id.btnPantry -> "pantryItem"
                    else -> segment
                }
            }
        }

        btnAdd.setOnClickListener {
            val icon = selectedIconRes.toString()
            val itemName = etItemName.text.toString()
            val quantity = etQuantity.text.toString().toIntOrNull() ?: 0
            val unitType = sUnitType.selectedItem.toString()
            val expiredDate = tvDate.text.toString()

            // Validasi jika nama item kosong
            if (itemName.isEmpty()) {
                // Tampilkan error atau pesan validasi jika perlu
                etItemName.error = "Item name cannot be empty"
                return@setOnClickListener
            }

            // Membuat map data untuk diupdate
            val data = mapOf(
                "name" to itemName,
                "stock" to quantity,
                "unitType" to unitType,
                "expiredDate" to expiredDate,
                "icon" to icon
            )

            // Update item jika itemId ada
            itemId?.let {
                itemViewModel.updateItem(it, data)
                finish()
            }
        }

        // Cancel button click listener
        btnCancel.setOnClickListener {
            finish()
        }
    }

    // Function to apply window insets for edge-to-edge support
    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to setup unit type spinner
    private fun setupUnitTypeSpinner() {
        ArrayAdapter.createFromResource(
            this, R.array.unit_type, android.R.layout.simple_spinner_item
        ).also { choiceList ->
            choiceList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sUnitType.adapter = choiceList
        }
    }

    // Function to set default toggle based on the category
    private fun setDefaultToggle(category: String) {
        when (category) {
            "fridgeItem" -> toggleGroup.check(R.id.btnFridge)
            "pantryItem" -> toggleGroup.check(R.id.btnPantry)
            else -> toggleGroup.check(R.id.btnFridge) // Default if category is unrecognized
        }
    }

    // Function to observe item data from ViewModel
    private fun observeItemData() {
        itemId?.let {
            // Fetch and observe item data
            val item: LiveData<Item?> = itemViewModel.getItemById(it)
            item.observe(this) { itemData ->
                itemData?.let {
                    // Populate the UI with the item data
                    it.icon?.let {
                        val iconRes = it.toIntOrNull() ?: R.drawable.stat_view
                        ivIcon.setImageResource(iconRes)
                    }
                    etItemName.setText(it.name)
                    etQuantity.setText(it.stock.toString())
                    tvDate.text = it.expiredDate

                    // Set the unitType in Spinner
                    val unitTypePosition = it.unitType?.let { it1 -> getUnitTypePosition(it1) }
                    if (unitTypePosition != null) {
                        sUnitType.setSelection(unitTypePosition)
                    }
                } ?: run {
                    // Clear fields if no data is found
                    etItemName.setText("")
                    etQuantity.setText("")
                    tvDate.text = ""
                }
            }
        }
    }

    // Function to get the position of unitType in the spinner array
    private fun getUnitTypePosition(unitType: String): Int {
        val unitTypeArray = resources.getStringArray(R.array.unit_type)
        return unitTypeArray.indexOf(unitType).takeIf { it >= 0 } ?: 0 // Default to 0 if not found
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
                val imageView = ImageView(this@EditItemActivity)
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

    // Function to show date picker
    private fun setupDatePicker() {
        btnDate.setOnClickListener {
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
                    tvDate.text = formattedDate
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }
}
