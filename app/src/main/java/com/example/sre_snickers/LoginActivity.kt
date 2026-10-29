package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean("isLogin", false)
        if (isLogin) {
            val intent = Intent(this, BerandaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }

        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        binding.btnLoginSubmit.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

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
                finish()
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Login Gagal")
                    .setMessage("Username atau password salah! Silahkan coba lagi.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
