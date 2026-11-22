package com.example.sre_snickers

import android.content.Intent
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
import com.example.sre_snickers.data.model.CartLine
import com.example.sre_snickers.data.model.CheckoutRequest
import com.example.sre_snickers.databinding.ActivityPesananBinding
import com.example.sre_snickers.util.CartManager
import com.example.sre_snickers.util.SessionManager
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import com.example.sre_snickers.util.toRupiah
import kotlinx.coroutines.launch

open class PesananActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPesananBinding
    private lateinit var adapter: CartAdapter

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
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, 0)
            insets
        }

        setupBottomNavigation(binding.bottomNavigation, R.id.nav_pesanan)
        setupToolbarLogout(binding.toolbar)
        setupWhatsAppFab(binding.fabWhatsApp)
        binding.toolbar.setNavigationOnClickListener { finish() }

        adapter = CartAdapter { refreshCart() }
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = adapter

        binding.cardRiwayat.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
        }
        binding.btnLihatMenu.setOnClickListener {
            val intent = Intent(this, DaftarMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
        binding.btnCheckout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Konfirmasi Pesanan")
                .setMessage("Total pembayaran ${CartManager.totalPrice().toRupiah()}.\nLanjutkan checkout?")
                .setPositiveButton("Ya, Pesan") { _, _ -> doCheckout() }
                .setNegativeButton("Batal", null)
                .show()
        }
    }

    private fun refreshCart() {
        val items = CartManager.list()
        val empty = items.isEmpty()
        binding.rvCart.visibility = if (empty) View.GONE else View.VISIBLE
        binding.layoutEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.cardSummary.visibility = if (empty) View.GONE else View.VISIBLE

        adapter.items = items
        adapter.notifyDataSetChanged()

        val total = CartManager.totalPrice()
        binding.tvItemCount.text = "${CartManager.totalQty()} item"
        binding.tvTotal.text = total.toRupiah()
        binding.tvPoinEstimasi.text = "+${total / 1000} poin"
        updateCartBadge(binding.bottomNavigation)
    }

    private fun doCheckout() {
        val session = SessionManager(this)
        val lines = CartManager.list().map { CartLine(it.menu.id, it.qty) }
        setLoading(true)
        lifecycleScope.launch {
            val result = apiCall { ApiClient.service.checkout(CheckoutRequest(session.userId, lines)) }
            setLoading(false)
            result
                .onSuccess { res ->
                    if (res.ok) {
                        res.poin_baru?.let { session.updatePoints(it) }
                        CartManager.clear()
                        refreshCart()
                        AlertDialog.Builder(this@PesananActivity)
                            .setTitle("Pesanan Berhasil 🎉")
                            .setMessage(
                                "Nomor pesanan: #${res.order_id}\n" +
                                        "Total: ${(res.total ?: 0).toRupiah()}\n" +
                                        "Poin didapat: +${res.poin_didapat ?: 0}"
                            )
                            .setPositiveButton("Lihat Riwayat") { _, _ ->
                                startActivity(Intent(this@PesananActivity, RiwayatActivity::class.java))
                            }
                            .setNegativeButton("Tutup", null)
                            .show()
                    } else {
                        Toast.makeText(this@PesananActivity, res.pesan, Toast.LENGTH_LONG).show()
                    }
                }
                .onFailure { Toast.makeText(this@PesananActivity, it.friendlyMessage(), Toast.LENGTH_LONG).show() }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.btnCheckout.isEnabled = !loading
        binding.btnCheckout.text = if (loading) "Memproses..." else "Checkout Sekarang"
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = R.id.nav_pesanan
        refreshCart()
    }
}