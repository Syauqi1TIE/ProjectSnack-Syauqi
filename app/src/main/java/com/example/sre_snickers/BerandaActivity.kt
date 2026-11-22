package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.data.model.GetUserRequest
import com.example.sre_snickers.data.model.PromoModel
import com.example.sre_snickers.databinding.ActivityBerandaBinding
import com.example.sre_snickers.util.SessionManager
import com.example.sre_snickers.util.apiCall
import kotlinx.coroutines.launch

open class BerandaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBerandaBinding
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = SessionManager(this)
        if (!session.isLoggedIn()) {
            session.logout() // bersihkan sisa data login lama
            val intent = Intent(this, PilihanLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, 0)
            insets
        }

        setupBottomNavigation(binding.bottomNavigation, R.id.nav_beranda)
        setupToolbarLogout(binding.toolbar)
        setupWhatsAppFab(binding.fabWhatsApp)
        binding.toolbar.setNavigationOnClickListener { finish() }

        loadBanners()
        loadUserData()

        binding.cardGoToProfile.setOnClickListener { open(ProfilMemberActivity::class.java) }
        binding.cardMember.setOnClickListener { open(ProfilMemberActivity::class.java) }
        binding.tvSeeAllMenu.setOnClickListener { open(DaftarMenuActivity::class.java) }
        binding.cardPromo.setOnClickListener { startActivity(Intent(this, PromoActivity::class.java)) }
        binding.cardRewards.setOnClickListener { startActivity(Intent(this, RewardsActivity::class.java)) }
        binding.layoutPointsBadge.setOnClickListener { startActivity(Intent(this, RewardsActivity::class.java)) }
        binding.cardHistory.setOnClickListener { startActivity(Intent(this, RiwayatActivity::class.java)) }
    }

    private fun open(target: Class<*>) {
        val intent = Intent(this, target)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = R.id.nav_beranda
        updateCartBadge(binding.bottomNavigation)
        loadUserData()
        refreshUserFromServer()
    }

    private fun loadUserData() {
        val sp = getSharedPreferences("user_pref", MODE_PRIVATE)
        val name = sp.getString("nama", "") ?: ""
        val username = sp.getString("username", "") ?: ""
        binding.tvGreetingName.text = if (name.isNotEmpty()) name else username
        binding.tvHeaderPoints.text = "${sp.getInt("member_points", 0)} Poin"
    }

    private fun refreshUserFromServer() {
        lifecycleScope.launch {
            apiCall { ApiClient.service.getUser(GetUserRequest(session.userId)) }
                .onSuccess { res ->
                    val user = res.user
                    if (res.ok && user != null) {
                        session.saveUser(user)
                        loadUserData()
                    }
                }
        }
    }

    private fun loadBanners() {
        lifecycleScope.launch {
            // Kalau server gagal, banner cadangan (gambar lokal) tetap tampil
            val list = apiCall { ApiClient.service.getPromo() }
                .getOrNull()?.takeIf { it.isNotEmpty() } ?: fallbackBanners()
            binding.bannerViewPager.adapter = BannerAdapter(list)
            binding.bannerDotIndicator.attachTo(binding.bannerViewPager)
        }
    }

    private fun fallbackBanners() = listOf(
        PromoModel(0, "PROMO SPESIAL", "Mulai Hari Dengan Rasa Terbaik", "Kopi Dan Roti Fresh Mulai Rp. 20K", "backgroundsobremessa"),
        PromoModel(0, "DISKON MEMBER", "Hemat 20% Untuk Semua Cake", "Gunakan Poin Member Setiap Hari", "cake"),
        PromoModel(0, "NEW BEVERAGE", "Coba Sensasi Kopi Cold Brew Baru", "Segar Nikmat Pas Untuk Bersantai", "drink")
    )
}