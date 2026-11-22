package com.example.sre_snickers

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.data.model.RedeemRequest
import com.example.sre_snickers.data.model.RewardModel
import com.example.sre_snickers.databinding.ActivityListBinding
import com.example.sre_snickers.util.SessionManager
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import kotlinx.coroutines.launch

class RewardsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var session: SessionManager
    private var adapter: RewardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = SessionManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        binding.toolbar.title = "Tukar Poin"
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.btnRetry.setOnClickListener { loadRewards() }
        loadRewards()
    }

    private fun currentPoints() = getSharedPreferences("user_pref", MODE_PRIVATE).getInt("member_points", 0)

    private fun showHeader() {
        binding.cardHeader.visibility = View.VISIBLE
        binding.tvHeaderTitle.text = "Poin kamu saat ini"
        binding.tvHeaderValue.text = "${currentPoints()} Poin"
    }

    private fun loadRewards() {
        binding.showLoading()
        showHeader()
        lifecycleScope.launch {
            apiCall { ApiClient.service.getRewards() }
                .onSuccess { list ->
                    if (list.isEmpty()) {
                        binding.showMessage("Belum ada hadiah tersedia.", false)
                    } else {
                        adapter = RewardAdapter(list, currentPoints()) { confirmRedeem(it) }
                        binding.rvList.adapter = adapter
                        binding.showList()
                    }
                }
                .onFailure { binding.showMessage(it.friendlyMessage(), true) }
        }
    }

    private fun confirmRedeem(reward: RewardModel) {
        AlertDialog.Builder(this)
            .setTitle("Tukar Poin")
            .setMessage("Tukar ${reward.poin} poin dengan\n\"${reward.nama}\"?")
            .setPositiveButton("Tukar") { _, _ -> redeem(reward) }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun redeem(reward: RewardModel) {
        lifecycleScope.launch {
            apiCall { ApiClient.service.tukarPoin(RedeemRequest(session.userId, reward.id)) }
                .onSuccess { res ->
                    if (res.ok) {
                        res.poin_baru?.let { session.updatePoints(it) }
                        showHeader()
                        adapter?.userPoin = currentPoints()
                        adapter?.notifyDataSetChanged()
                        AlertDialog.Builder(this@RewardsActivity)
                            .setTitle("Penukaran Berhasil 🎁")
                            .setMessage("Tunjukkan kode ini ke kasir:\n\n${res.kode}")
                            .setPositiveButton("OK", null)
                            .show()
                    } else {
                        Toast.makeText(this@RewardsActivity, res.pesan, Toast.LENGTH_LONG).show()
                    }
                }
                .onFailure { Toast.makeText(this@RewardsActivity, it.friendlyMessage(), Toast.LENGTH_LONG).show() }
        }
    }
}