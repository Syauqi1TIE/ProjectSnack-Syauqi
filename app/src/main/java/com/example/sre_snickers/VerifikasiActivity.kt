package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.data.model.RegisterRequest
import com.example.sre_snickers.databinding.ActivityVerifikasiBinding
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import kotlinx.coroutines.launch

class VerifikasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        val nama = intent.getStringExtra("nama") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val dob = intent.getStringExtra("dob") ?: ""
        val gender = intent.getStringExtra("gender") ?: ""
        val username = intent.getStringExtra("username") ?: ""
        val password = intent.getStringExtra("password") ?: ""

        binding.tvValNama.text = nama
        binding.tvValEmail.text = email
        binding.tvValDob.text = dob
        binding.tvValGender.text = gender
        binding.tvValUsername.text = username
        // Password disamarkan
        binding.tvValPassword.text = "•".repeat(password.length)
        binding.tvValConfirmPassword.text = "•".repeat(password.length)

        binding.btnKembali.setOnClickListener { finish() }

        binding.btnSubmit.setOnClickListener {
            setLoading(true)
            lifecycleScope.launch {
                val result = apiCall {
                    ApiClient.service.register(
                        RegisterRequest(nama, email, dob, gender, username, password)
                    )
                }
                setLoading(false)
                result
                    .onSuccess { res -> if (res.ok) showSuccess() else showError(res.pesan) }
                    .onFailure { showError(it.friendlyMessage()) }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.btnSubmit.isEnabled = !loading
        binding.btnKembali.isEnabled = !loading
        binding.btnSubmit.text = if (loading) "Memproses..." else "Submit"
    }

    private fun showSuccess() {
        AlertDialog.Builder(this)
            .setTitle("Registrasi Berhasil")
            .setMessage("Akun kamu sudah dibuat. Silakan login.")
            .setPositiveButton("Ke Halaman Login") { _, _ ->
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun showError(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Registrasi Gagal")
            .setMessage(message)
            .setPositiveButton("OK") { d, _ -> d.dismiss() }
            .setNegativeButton("Kembali ke Registrasi") { _, _ -> finish() }
            .show()
    }
}