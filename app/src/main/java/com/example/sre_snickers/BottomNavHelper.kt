package com.example.sre_snickers

import android.app.Activity
import android.content.Intent
import android.os.Build
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Activity.setupBottomNavigation(bottomNav: BottomNavigationView, currentItemId: Int) {
    if (currentItemId != 0 && bottomNav.menu.findItem(currentItemId) != null) {
        bottomNav.selectedItemId = currentItemId
    }

    bottomNav.setOnItemSelectedListener { item ->
        if (item.itemId == currentItemId) {
            return@setOnItemSelectedListener true
        }

        val targetClass = when (item.itemId) {
            R.id.nav_profile -> ProfileActivity::class.java
            R.id.nav_login -> PilihanLoginActivity::class.java
            R.id.nav_snack -> Snack::class.java
            else -> null
        }

        if (targetClass != null) {
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
