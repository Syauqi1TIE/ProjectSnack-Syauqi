package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityBerandaBinding

open class BerandaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBerandaBinding

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
        binding = ActivityBerandaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupBottomNavigation(binding.bottomNavigation, R.id.nav_beranda)
        setupToolbarLogout(binding.toolbar)
        setupWhatsAppFab(binding.fabWhatsApp)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        loadUserData()

        binding.cardGoToProfile.setOnClickListener {
            val intent = Intent(this, ProfilMemberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }

        binding.tvSeeAllMenu.setOnClickListener {
            val intent = Intent(this, DaftarMenuActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = R.id.nav_beranda
        loadUserData()
    }

    private fun loadUserData() {
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val name = sharedPref.getString("nama", "") ?: ""
        val username = sharedPref.getString("username", "") ?: ""
        val displayName = if (name.isNotEmpty()) name else if (username.isNotEmpty()) username else "Poki ganteng"
        val points = sharedPref.getInt("member_points", 250)

        binding.tvGreetingName.text = displayName
        binding.tvHeaderPoints.text = "$points Poin"
    }
}
