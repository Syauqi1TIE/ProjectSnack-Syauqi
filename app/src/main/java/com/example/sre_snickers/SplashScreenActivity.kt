package com.example.sre_snickers

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val logo = findViewById<ImageView>(R.id.imgSplashLogo)
        val brand = findViewById<TextView>(R.id.tvSplashBrand)
        val tagline = findViewById<TextView>(R.id.tvSplashTagline)

        // Animasi muncul: fade-in + sedikit membesar
        logo.alpha = 0f
        logo.scaleX = 0.8f
        logo.scaleY = 0.8f
        logo.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(800).start()

        brand.alpha = 0f
        brand.animate().alpha(1f).setStartDelay(400).setDuration(800).start()

        tagline.alpha = 0f
        tagline.animate().alpha(1f).setStartDelay(700).setDuration(800).start()

        lifecycleScope.launch {
            delay(4000) // simulasi pengambilan data selama 2 detik

            val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)
            val isLogin = sharedPref.getBoolean("isLogin", false)

            val target = if (isLogin) MainActivity::class.java else PilihanLoginActivity::class.java
            startActivity(Intent(this@SplashScreenActivity, target))
            finish()
        }
    }
}