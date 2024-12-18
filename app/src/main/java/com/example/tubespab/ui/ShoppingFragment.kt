package com.example.tubespab.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tubespab.R
import com.example.tubespab.adapter.ShoppingAdapter
import com.example.tubespab.model.ShopItem
import com.example.tubespab.repository.CartRepository
import com.example.tubespab.repository.ShopItemRepository
import com.example.tubespab.util.AuthController
import com.example.tubespab.util.NavbarController
import com.example.tubespab.viewmodel.CartViewModel
import com.example.tubespab.viewmodel.CartViewModelFactory
import com.example.tubespab.viewmodel.ShopItemViewModel
import com.example.tubespab.viewmodel.ShopItemViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShoppingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var shoppingAdapter: ShoppingAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var shopItemViewModel: ShopItemViewModel
    private val cartId = AuthController.getCurrentUserUid()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        cartViewModel = ViewModelProvider(this, CartViewModelFactory(CartRepository())).get(CartViewModel::class.java)
        shopItemViewModel = ViewModelProvider(this, ShopItemViewModelFactory(ShopItemRepository())).get(ShopItemViewModel::class.java)

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.shoppingList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        shoppingAdapter = ShoppingAdapter(emptyList())
        recyclerView.adapter = shoppingAdapter

        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    filterShoppingData(it)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterShoppingData(it)
                }
                return true
            }
        })

        filterShoppingData(searchView.query.toString())

        val btnShowInput: ImageButton = view.findViewById(R.id.btnAdd)
        btnShowInput.setOnClickListener {
            showInputDialog()
        }
    }

    private fun filterShoppingData(query: String) {
        if (cartId != null) {
            cartViewModel.getItemByCartId(cartId)
                .observe(viewLifecycleOwner) { shoppingList ->
                    val filteredList = shoppingList?.filter { shopItem ->
                        shopItem.name.contains(query, ignoreCase = true)
                    }
                    filteredList?.let {
                        shoppingAdapter.updateData(it)
                    }
                }
        }
    }

    private fun showInputDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.add_shopping_item, null)

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(dialogView)

        val etItemName = dialogView.findViewById<EditText>(R.id.etItemName)
        val etQuantity = dialogView.findViewById<EditText>(R.id.etQuantity)

        val sUnitType = dialogView.findViewById<Spinner>(R.id.unitType)
        ArrayAdapter.createFromResource(
            requireContext(), R.array.unit_type, android.R.layout.simple_spinner_item
        ).also {
                choiceList -> choiceList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sUnitType.adapter = choiceList
        }
        bottomSheetDialog.show()

        val btnAddItem = dialogView.findViewById<ImageButton>(R.id.btnAdd)
        btnAddItem.setOnClickListener {
            val shopItem = ShopItem(
                etItemName.text.toString(),
                etQuantity.text.toString().toInt(),
                sUnitType.selectedItem.toString(),
            )
            shopItemViewModel.addShopItem(shopItem) { shopItemId ->
                if (cartId != null) {
                    cartViewModel.addCartItem(cartId, shopItemId)
                }
            }
        }
    }
}
