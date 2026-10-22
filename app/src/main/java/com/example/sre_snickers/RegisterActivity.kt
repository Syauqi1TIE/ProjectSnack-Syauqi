package com.example.sre_snickers

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityRegisterBinding
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Pre-fill fields if data exists in SharedPreferences (misal kembali dari Halaman Verifikasi)
        binding.etFullName.setText(sharedPref.getString("nama", ""))
        binding.etEmail.setText(sharedPref.getString("email", ""))
        binding.etDob.setText(sharedPref.getString("tanggal_lahir", ""))
        val savedGender = sharedPref.getString("jenis_kelamin", "")
        if (savedGender == "Laki-laki") {
            binding.rbMale.isChecked = true
        } else if (savedGender == "Perempuan") {
            binding.rbFemale.isChecked = true
        }
        binding.etUsername.setText(sharedPref.getString("username", ""))
        binding.etRegisterPassword.setText(sharedPref.getString("password", ""))
        binding.etConfirmPassword.setText(sharedPref.getString("confirm_password", ""))

        // DatePicker untuk Tanggal Lahir
        binding.etDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val dateFormatted = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.etDob.setText(dateFormatted)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        // Tombol Selanjutnya -> Simpan ke SharedPreferences dan Pindah ke Halaman Verifikasi
        binding.btnRegisterSubmit.setOnClickListener {
            val nama = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val dob = binding.etDob.text.toString().trim()
            val selectedGenderId = binding.rgGender.checkedRadioButtonId
            val gender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            // Simpan ke SharedPreferences untuk setiap isiannya sesuai soal
            val editor = sharedPref.edit()
            editor.putString("nama", nama)
            editor.putString("email", email)
            editor.putString("tanggal_lahir", dob)
            editor.putString("jenis_kelamin", gender)
            editor.putString("username", username)
            editor.putString("password", password)
            editor.putString("confirm_password", confirmPassword)
            editor.apply()

            Toast.makeText(this, "Data registrasi disimpan, membuka verifikasi...", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, VerifikasiActivity::class.java)
            startActivity(intent)
        }

        binding.tvGoToLogin.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
