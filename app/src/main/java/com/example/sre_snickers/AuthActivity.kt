package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Kode ini harus selalu dipanggil saat butuh akses "user_pref"
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Kondisi jika isLogin bernilai true
        val isLogin = sharedPref.getBoolean("isLogin", false)
        if (isLogin) {
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Ambil data username dan password dari SharedPreferences
            val spUsername = sharedPref.getString("username", "") ?: ""
            val spPassword = sharedPref.getString("password", "") ?: ""

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Silakan isi Username dan Password!", Toast.LENGTH_SHORT).show()
            } else if (spUsername.isNotEmpty() && username == spUsername && password == spPassword) {
                // Rule: username == username di SharedPreferences dan password == password di SharedPreferences
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("current_username", username)
                editor.apply()

                Toast.makeText(this, "Login Berhasil! Selamat datang, $username", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, BerandaActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username atau password salah! Silahkan coba lagi.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }
}
