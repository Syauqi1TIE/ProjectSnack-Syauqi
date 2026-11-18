package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sre_snickers.databinding.ActivityDaftarMenuBinding

open class DaftarMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarMenuBinding
    private var selectedCategory: String = "Semua"
    private var searchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean("isLogin", false)
        if (!isLogin) {
            val intent = Intent(this, PilihanLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityDaftarMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupBottomNavigation(binding.bottomNavigation, R.id.nav_daftar_menu)
        setupToolbarLogout(binding.toolbar)
        setupWhatsAppFab(binding.fabWhatsApp)

        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        updateProductList()

        setupCategoryChips()
        setupSearch()

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun updateProductList() {
        val filteredList = MenuData.products.filter { product ->
            val matchesCategory = if (selectedCategory == "Semua") true else product.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = if (searchQuery.isEmpty()) true else product.name.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }

        binding.rvProducts.adapter = ProductAdapter(filteredList) { product ->
            Toast.makeText(
                this,
                "${product.name} (Rp ${"%,d".format(product.price).replace(',', '.')}) dipilih",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun setupCategoryChips() {
        val chips = listOf(
            binding.chipAll to "Semua",
            binding.chipCoffee to "Coffee",
            binding.chipPastry to "Pastry",
            binding.chipCake to "Cake"
        )

        chips.forEach { (chip, category) ->
            chip.setOnClickListener {
                selectedCategory = category
                chips.forEach { (c, cat) ->
                    if (cat == selectedCategory) {
                        c.setBackgroundResource(R.drawable.bg_button_primary)
                        c.setTextColor(ContextCompat.getColor(this, R.color.white))
                    } else {
                        c.setBackgroundResource(R.drawable.bg_button_secondary)
                        c.setTextColor(ContextCompat.getColor(this, R.color.brand_dark_brown))
                    }
                }
                updateProductList()
            }
        }
    }

    private fun setupSearch() {
        binding.etSearchMenu.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s.toString().trim()
                updateProductList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = R.id.nav_daftar_menu
    }
}
