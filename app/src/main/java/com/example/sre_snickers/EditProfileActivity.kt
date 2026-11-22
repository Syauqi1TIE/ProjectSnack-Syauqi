package com.example.sre_snickers

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.sre_snickers.data.api.ApiClient
import com.example.sre_snickers.data.model.UpdateProfileRequest
import com.example.sre_snickers.databinding.ActivityEditProfileBinding
import com.example.sre_snickers.util.SessionManager
import com.example.sre_snickers.util.apiCall
import com.example.sre_snickers.util.friendlyMessage
import kotlinx.coroutines.launch
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val b = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(b.left, b.top, b.right, 0)
            insets
        }

        binding.toolbar.setNavigationOnClickListener { finish() }

        val session = SessionManager(this)
        val sp = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Isi data awal (password tidak pernah disimpan di HP)
        binding.etEditFullName.setText(sp.getString("nama", ""))
        binding.etEditEmail.setText(sp.getString("email", ""))
        binding.etEditEmail.isEnabled = false
        binding.etEditDob.setText(sp.getString("tanggal_lahir", ""))
        when (sp.getString("jenis_kelamin", "")) {
            "Laki-laki" -> binding.rbEditMale.isChecked = true
            "Perempuan" -> binding.rbEditFemale.isChecked = true
        }
        binding.etEditUsername.setText(sp.getString("username", ""))
        binding.etEditPassword.hint = "Kosongkan jika tidak diganti"
        binding.etEditConfirmPassword.hint = "Ulangi password baru"

        binding.etEditDob.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                binding.etEditDob.setText(String.format("%02d/%02d/%04d", d, m + 1, y))
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnSaveEditProfile.setOnClickListener {
            val nama = binding.etEditFullName.text.toString().trim()
            val dob = binding.etEditDob.text.toString().trim()
            val genderId = binding.rgEditGender.checkedRadioButtonId
            val gender = if (genderId != -1) findViewById<RadioButton>(genderId).text.toString() else ""
            val username = binding.etEditUsername.text.toString().trim()
            val password = binding.etEditPassword.text.toString().trim()
            val confirm = binding.etEditConfirmPassword.text.toString().trim()

            when {
                nama.isEmpty() || username.isEmpty() ->
                    toast("Nama dan Username tidak boleh kosong!")
                password.isNotEmpty() && password.length < 6 ->
                    toast("Password baru minimal 6 karakter")
                password != confirm ->
                    toast("Password dan Konfirmasi Password tidak sama!")
                else -> {
                    setLoading(true)
                    lifecycleScope.launch {
                        val result = apiCall {
                            ApiClient.service.updateProfile(
                                UpdateProfileRequest(session.userId, nama, dob, gender, username, password)
                            )
                        }
                        setLoading(false)
                        result
                            .onSuccess { res ->
                                val user = res.user
                                if (res.ok && user != null) {
                                    session.saveUser(user)
                                    toast("Profil berhasil diperbarui!")
                                    finish()
                                } else {
                                    toast(res.pesan)
                                }
                            }
                            .onFailure { toast(it.friendlyMessage()) }
                    }
                }
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.btnSaveEditProfile.isEnabled = !loading
        binding.btnSaveEditProfile.text = if (loading) "Menyimpan..." else "Simpan Perubahan"
    }

    private fun toast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}