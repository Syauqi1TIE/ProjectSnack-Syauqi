package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.data.model.MenuModel
import com.example.sre_snickers.databinding.ActivityDaftarMenuBinding
import com.example.sre_snickers.util.CartManager
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

open class DaftarMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDaftarMenuBinding
    private var allMenu: List<MenuModel> = emptyList()
    private var selectedCategory = "Semua"
    private var searchQuery = ""
    private val categories = listOf("Semua", "Coffee", "Pastry", "Cake")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        if (!sharedPref.getBoolean("isLogin", false)) {
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
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, 0)
            insets
        }

        setupBottomNavigation(binding.bottomNavigation, R.id.nav_daftar_menu)
        setupToolbarLogout(binding.toolbar)
        setupWhatsAppFab(binding.fabWhatsApp)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.rvProducts.layoutManager = GridLayoutManager(this, 2)
        setupTabLayout()
        setupSearch()
        binding.btnRetry.setOnClickListener { loadMenu() }

        loadMenu()
    }

    private fun loadMenu() {
        binding.progressMenu.visibility = View.VISIBLE
        binding.layoutError.visibility = View.GONE
        binding.rvProducts.visibility = View.GONE

        lifecycleScope.launch {
            apiCall { ApiClient.service.getMenu() }
                .onSuccess {
                    allMenu = it
                    binding.progressMenu.visibility = View.GONE
                    binding.rvProducts.visibility = View.VISIBLE
                    updateBadges()
                    updateProductList()
                }
                .onFailure {
                    binding.progressMenu.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvError.text = it.friendlyMessage()
                }
        }
    }

    private fun updateProductList() {
        val filtered = allMenu.filter { p ->
            val okCategory = selectedCategory == "Semua" || p.kategori.equals(selectedCategory, true)
            val okSearch = searchQuery.isEmpty() || p.nama.contains(searchQuery, true)
            okCategory && okSearch
        }
        binding.rvProducts.adapter = ProductAdapter(filtered) { menu ->
            CartManager.add(menu)
            Toast.makeText(this, "${menu.nama} masuk keranjang", Toast.LENGTH_SHORT).show()
            updateCartBadge(binding.bottomNavigation)
        }
    }

    private fun setupTabLayout() {
        categories.forEach { category ->
            val tab = binding.tabLayoutMenu.newTab().setText(category)
            tab.icon = ContextCompat.getDrawable(
                this, if (category == "Semua") R.drawable.ic_nav_menu else R.drawable.ic_nav_snack
            )
            binding.tabLayoutMenu.addTab(tab)
        }
        binding.tabLayoutMenu.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectedCategory = categories.getOrElse(tab?.position ?: 0) { "Semua" }
                updateProductList()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun updateBadges() {
        categories.forEachIndexed { i, cat ->
            val count = if (cat == "Semua") allMenu.size else allMenu.count { it.kategori.equals(cat, true) }
            binding.tabLayoutMenu.getTabAt(i)?.orCreateBadge?.apply {
                number = count
                backgroundColor = ContextCompat.getColor(this@DaftarMenuActivity, R.color.brand_terracotta)
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
        updateCartBadge(binding.bottomNavigation)
    }
}