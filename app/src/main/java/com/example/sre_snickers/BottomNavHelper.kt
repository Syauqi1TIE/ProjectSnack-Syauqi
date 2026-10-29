package com.example.sre_snickers

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun Activity.setupBottomNavigation(bottomNav: BottomNavigationView, currentItemId: Int) {
    if (currentItemId != 0 && bottomNav.menu.findItem(currentItemId) != null) {
        bottomNav.selectedItemId = currentItemId
    }

    bottomNav.setOnItemSelectedListener { item ->
        if (item.itemId == currentItemId) {
            return@setOnItemSelectedListener true
        }

        val sharedPref = getSharedPreferences("user_pref", Activity.MODE_PRIVATE)
        val isLogin = sharedPref.getBoolean("isLogin", false)

        if (!isLogin) {
            Toast.makeText(this, "Silakan login atau registrasi terlebih dahulu!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PilihanLoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return@setOnItemSelectedListener false
        }

        val targetClass = when (item.itemId) {
            R.id.nav_beranda -> BerandaActivity::class.java
            R.id.nav_daftar_menu -> DaftarMenuActivity::class.java
            R.id.nav_pesanan -> PesananActivity::class.java
            else -> null
        }

        if (targetClass != null && targetClass != this::class.java) {
            val intent = Intent(this, targetClass)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(Activity.OVERRIDE_TRANSITION_OPEN, 0, 0)
                overrideActivityTransition(Activity.OVERRIDE_TRANSITION_CLOSE, 0, 0)
            } else {
                @Suppress("DEPRECATION")
                overridePendingTransition(0, 0)
            }
            true
        } else {
            false
        }
    }
}

fun Activity.setupToolbarLogout(toolbar: MaterialToolbar) {
    toolbar.setOnMenuItemClickListener { menuItem ->
        when (menuItem.itemId) {
            R.id.action_developer_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                startActivity(intent)
                true
            }
            R.id.action_logout -> {
                AlertDialog.Builder(this)
                    .setTitle("Logout")
                    .setMessage("Apakah kamu yakin ingin logout?")
                    .setPositiveButton("Ya") { dialog, _ ->
                        val sharedPref = getSharedPreferences("user_pref", Activity.MODE_PRIVATE)
                        val editor = sharedPref.edit()
                        editor.putBoolean("isLogin", false)
                        editor.remove("current_username")
                        editor.apply()
                        dialog.dismiss()

                        val intent = Intent(this, SplashScreenActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                    .setNegativeButton("Tidak") { dialog, _ -> dialog.dismiss() }
                    .show()
                true
            }
            else -> false
        }
    }
}

fun Activity.setupWhatsAppFab(fab: FloatingActionButton) {
    fab.setOnClickListener {
        val phoneNumber = "6287714892728"
        val url = "https://wa.me/$phoneNumber?text=Halo%20Sobremesa,%20saya%20ingin%20bertanya"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Tidak dapat membuka WhatsApp", Toast.LENGTH_SHORT).show()
        }
    }
}
