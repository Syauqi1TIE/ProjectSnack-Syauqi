
package com.example.sre_snickers
//oki
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                0
            )

            insets
        }

        // Bottom Navigation
        binding.bottomNavigation.setOnItemSelectedListener { item ->

            when (item.itemId) {

                // Menu Profil
                R.id.nav_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                // Menu Akun
                R.id.nav_login -> {
                    val intent = Intent(this, PilihanLoginActivity::class.java)
                    startActivity(intent)
                    true
                }

                // Menu Snack
                R.id.nav_snack -> {
                    val intent = Intent(this, Snack::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }

        // Tombol Jelajahi Snack
        binding.btnToSnack.setOnClickListener {
            val intent = Intent(this, Snack::class.java)
            startActivity(intent)
        }

        // Shared Preferences
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Tombol Logout
        binding.btnLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Apakah kamu yakin ingin logout?")

                .setPositiveButton("Ya") { dialog, _ ->

                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()

                    dialog.dismiss()

                    val intent = Intent(
                        this,
                        SplashScreenActivity::class.java
                    )

                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }

                .setNegativeButton("Tidak") { dialog, _ ->
                    dialog.dismiss()
                }

                .show()
        }
    }
}
