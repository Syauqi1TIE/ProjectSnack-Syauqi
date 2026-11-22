package com.example.sre_snickers

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityProfilMemberBinding
import java.util.Calendar

open class ProfilMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilMemberBinding

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
        binding = ActivityProfilMemberBinding.inflate(layoutInflater)
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

        loadProfileData()

        binding.btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        binding.btnLogoutMember.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah kamu yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ -> dialog.dismiss(); performLogout() }
                .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = R.id.nav_beranda
        loadProfileData()
    }

    private fun loadProfileData() {
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val name = sharedPref.getString("nama", "Nama Lengkap") ?: "Nama Lengkap"
        val username = sharedPref.getString("username", "username") ?: "username"
        val points = sharedPref.getInt("member_points", 250)

        binding.tvProfileName.text = if (name.isEmpty()) "Nama Lengkap" else name
        binding.tvProfileUsername.text = if (username.isEmpty()) "@username" else "@$username"
        binding.tvPoints.text = "$points Points"
    }

    private fun showEditProfileDialog() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }
}
