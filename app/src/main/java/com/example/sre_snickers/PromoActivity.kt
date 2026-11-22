package com.example.sre_snickers

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.databinding.ActivityListBinding
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import kotlinx.coroutines.launch

class PromoActivity : AppCompatActivity() {
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

        binding.toolbar.title = "Promo Member"
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.btnRetry.setOnClickListener { loadPromo() }
        loadPromo()
    }

    private fun loadPromo() {
        binding.showLoading()
        lifecycleScope.launch {
            apiCall { ApiClient.service.getPromo() }
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        binding.showMessage("Belum ada promo saat ini.", false)
                    } else {
                        binding.rvList.adapter = PromoAdapter(list)
                        binding.showList()
                    }
                }
                .onFailure { binding.showMessage(it.friendlyMessage(), true) }
        }
    }
}