package com.example.sre_snickers

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.databinding.ActivityListBinding
import com.example.sre_snickers.util.SessionManager
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import com.example.sre_snickers.util.toRupiah
import kotlinx.coroutines.launch

class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        binding.toolbar.title = "Riwayat Transaksi"
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.btnRetry.setOnClickListener { loadOrders() }
        loadOrders()
    }

    private fun loadOrders() {
        binding.showLoading()
        val userId = SessionManager(this).userId
        lifecycleScope.launch {
            apiCall { ApiClient.service.getOrders("eq.$userId") }
                .onSuccess { orders ->
                    if (orders.isEmpty()) {
                        binding.showMessage("Belum ada transaksi.\nYuk pesan menu favoritmu!", false)
                    } else {
                        binding.cardHeader.visibility = View.VISIBLE
                        binding.tvHeaderTitle.text = "Total belanja (${orders.size} transaksi)"
                        binding.tvHeaderValue.text = orders.sumOf { it.total }.toRupiah()
                        binding.rvList.adapter = OrderAdapter(orders)
                        binding.showList()
                    }
                }
                .onFailure { binding.showMessage(it.friendlyMessage(), true) }
        }
    }
}