package com.example.tubespab.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.adapter.InventoryAdapter
import com.example.tubespab.repository.InventoryRepository
import com.example.tubespab.util.AuthController
import com.example.tubespab.viewmodel.InventoryViewModel
import com.example.tubespab.viewmodel.InventoryViewModelFactory
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InventoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var inventoryAdapter: InventoryAdapter
    private val inventoryViewModel: InventoryViewModel by viewModels {
        InventoryViewModelFactory(InventoryRepository())
    }

    private var currentCategory: String = "fridgeItem"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inventory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.inventoryList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        inventoryAdapter = InventoryAdapter(emptyList())
        recyclerView.adapter = inventoryAdapter

        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterInventoryData(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterInventoryData(it) }
                return true
            }
        })

        val toggleGroup: MaterialButtonToggleGroup = view.findViewById(R.id.toggleGroup)
        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnFridge -> {
                        currentCategory = "fridgeItem"
                        filterInventoryData(searchView.query.toString())
                    }
                    R.id.btnPantry -> {
                        currentCategory = "pantryItem"
                        filterInventoryData(searchView.query.toString())
                    }
                }
            }
        }
        toggleGroup.check(R.id.btnFridge)

        val fab: FloatingActionButton = view.findViewById(R.id.fabAdd)
        fab.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun filterInventoryData(query: String) {
        val inventoryId = AuthController.getCurrentUserUid()
        if (inventoryId != null) {
            inventoryViewModel.getSegmentItems(inventoryId, currentCategory)
                .observe(viewLifecycleOwner) { inventoryList ->
                    val filteredList = inventoryList?.filter { item ->
                        item.name.contains(query, ignoreCase = true)
                    }
                    filteredList?.let {
                        inventoryAdapter.updateData(it)
                    }
                }
        }
    }
}