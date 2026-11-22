package com.example.sre_snickers

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
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

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, b.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.etDob.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                binding.etDob.setText(String.format("%02d/%02d/%04d", d, m + 1, y))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnRegisterSubmit.setOnClickListener {
            val nama = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val dob = binding.etDob.text.toString().trim()
            val genderId = binding.rgGender.checkedRadioButtonId
            val gender = if (genderId != -1) findViewById<RadioButton>(genderId).text.toString() else ""
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etRegisterPassword.text.toString().trim()
            val confirm = binding.etConfirmPassword.text.toString().trim()

            when {
                listOf(nama, email, dob, gender, username, password, confirm).any { it.isEmpty() } ->
                    Toast.makeText(this, "Semua data wajib diisi!", Toast.LENGTH_SHORT).show()
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    Toast.makeText(this, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                password.length < 6 ->
                    Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                password != confirm ->
                    Toast.makeText(this, "Password dan konfirmasi tidak sama", Toast.LENGTH_SHORT).show()
                else -> {
                    // Data dikirim lewat Intent, TIDAK disimpan di HP
                    val intent = Intent(this, VerifikasiActivity::class.java)
                    intent.putExtra("nama", nama)
                    intent.putExtra("email", email)
                    intent.putExtra("dob", dob)
                    intent.putExtra("gender", gender)
                    intent.putExtra("username", username)
                    intent.putExtra("password", password)
                    startActivity(intent)
                }
            }
        }

        binding.tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}