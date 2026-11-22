package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.data.model.LoginRequest
import com.example.sre_snickers.databinding.ActivityAuthBinding
import com.example.sre_snickers.util.SessionManager
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        val session = SessionManager(this)
        if (session.isLoggedIn()) {
            startActivity(Intent(this, BerandaActivity::class.java))
            finish()
            return
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Silakan isi Username dan Password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            setLoading(true)
            lifecycleScope.launch {
                val result = apiCall { ApiClient.service.login(LoginRequest(username, password)) }
                setLoading(false)
                result
                    .onSuccess { res ->
                        val user = res.user
                        if (res.ok && user != null) {
                            session.saveUser(user)
                            Toast.makeText(this@AuthActivity, "Selamat datang, ${user.nama}!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@AuthActivity, BerandaActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            showFail(res.pesan)
                        }
                    }
                    .onFailure { showFail(it.friendlyMessage()) }
            }
        }

        binding.tvGoToRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.btnLogin.isEnabled = !loading
        binding.btnLogin.text = if (loading) "MEMPROSES..." else "SIGN IN"
    }

    private fun showFail(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Login Gagal")
            .setMessage(message)
            .setPositiveButton("OK") { d, _ -> d.dismiss() }
            .show()
    }
}