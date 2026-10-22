package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityVerifikasiBinding

class VerifikasiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerifikasiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVerifikasiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Ambil data dari SharedPreferences
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
        val nama = sharedPref.getString("nama", "") ?: ""
        val email = sharedPref.getString("email", "") ?: ""
        val dob = sharedPref.getString("tanggal_lahir", "") ?: ""
        val gender = sharedPref.getString("jenis_kelamin", "") ?: ""
        val username = sharedPref.getString("username", "") ?: ""
        val password = sharedPref.getString("password", "") ?: ""
        val confirmPassword = sharedPref.getString("confirm_password", "") ?: ""

        // Tampilkan seluruh isian di TextView
        binding.tvValNama.text = if (nama.isEmpty()) "(kosong)" else nama
        binding.tvValEmail.text = if (email.isEmpty()) "(kosong)" else email
        binding.tvValDob.text = if (dob.isEmpty()) "(kosong)" else dob
        binding.tvValGender.text = if (gender.isEmpty()) "(kosong)" else gender
        binding.tvValUsername.text = if (username.isEmpty()) "(kosong)" else username
        binding.tvValPassword.text = if (password.isEmpty()) "(kosong)" else password
        binding.tvValConfirmPassword.text = if (confirmPassword.isEmpty()) "(kosong)" else confirmPassword

        // Tombol Kembali -> Untuk kembali ke halaman registrasi untuk memperbaiki isian di SP
        binding.btnKembali.setOnClickListener {
            finish()
        }

        // Tombol Submit -> Lakukan validasi data yang sudah ada di SharedPreferences
        binding.btnSubmit.setOnClickListener {
            // Validasi 1: Semua inputan tidak boleh kosong
            if (nama.isEmpty() || email.isEmpty() || dob.isEmpty() || gender.isEmpty() ||
                username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {

                AlertDialog.Builder(this)
                    .setTitle("Validasi Gagal")
                    .setMessage("Semua inputan tidak boleh kosong! Silakan tekan tombol Kembali untuk melengkapi data registrasi.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .setNegativeButton("Kembali ke Registrasi") { _, _ -> finish() }
                    .show()
                return@setOnClickListener
            }

            // Validasi 2: Password dan Confirm Password harus sama
            if (password != confirmPassword) {
                AlertDialog.Builder(this)
                    .setTitle("Validasi Gagal")
                    .setMessage("Password dan Confirm Password tidak sama! Silakan tekan tombol Kembali untuk memperbaikinya.")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .setNegativeButton("Kembali ke Registrasi") { _, _ -> finish() }
                    .show()
                return@setOnClickListener
            }

            // Jika berhasil melewati validasi
            val editor = sharedPref.edit()
            editor.putBoolean("isRegistered", true)
            editor.apply()

            AlertDialog.Builder(this)
                .setTitle("Registrasi Berhasil")
                .setMessage("Selamat! Data registrasi kamu telah terverifikasi dan berhasil disimpan.")
                .setPositiveButton("Ke Halaman Login") { _, _ ->
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setCancelable(false)
                .show()
        }
    }
}
