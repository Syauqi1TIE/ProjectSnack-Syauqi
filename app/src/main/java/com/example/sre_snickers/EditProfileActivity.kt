package com.example.sre_snickers

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityEditProfileBinding
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Pre-fill existing data
        binding.etEditFullName.setText(sharedPref.getString("nama", ""))
        val email = sharedPref.getString("email", "") ?: ""
        binding.etEditEmail.setText(email)
        binding.etEditEmail.isEnabled = false // Email / Gmail tidak dapat diubah

        binding.etEditDob.setText(sharedPref.getString("tanggal_lahir", ""))
        val savedGender = sharedPref.getString("jenis_kelamin", "")
        if (savedGender == "Laki-laki") {
            binding.rbEditMale.isChecked = true
        } else if (savedGender == "Perempuan") {
            binding.rbEditFemale.isChecked = true
        }
        binding.etEditUsername.setText(sharedPref.getString("username", ""))
        binding.etEditPassword.setText(sharedPref.getString("password", ""))
        binding.etEditConfirmPassword.setText(sharedPref.getString("confirm_password", ""))

        // DatePicker for DOB
        binding.etEditDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val dateFormatted = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                    binding.etEditDob.setText(dateFormatted)
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.btnSaveEditProfile.setOnClickListener {
            val newName = binding.etEditFullName.text.toString().trim()
            val newDob = binding.etEditDob.text.toString().trim()
            val selectedGenderId = binding.rgEditGender.checkedRadioButtonId
            val newGender = if (selectedGenderId != -1) {
                findViewById<RadioButton>(selectedGenderId).text.toString()
            } else {
                ""
            }
            val newUsername = binding.etEditUsername.text.toString().trim()
            val newPassword = binding.etEditPassword.text.toString().trim()
            val newConfirmPassword = binding.etEditConfirmPassword.text.toString().trim()

            if (newName.isEmpty() || newUsername.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(this, "Nama, Username, dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != newConfirmPassword) {
                Toast.makeText(this, "Password dan Konfirmasi Password tidak sama!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val editor = sharedPref.edit()
            editor.putString("nama", newName)
            editor.putString("tanggal_lahir", newDob)
            editor.putString("jenis_kelamin", newGender)
            editor.putString("username", newUsername)
            editor.putString("password", newPassword)
            editor.putString("confirm_password", newConfirmPassword)
            editor.apply()

            Toast.makeText(this, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
