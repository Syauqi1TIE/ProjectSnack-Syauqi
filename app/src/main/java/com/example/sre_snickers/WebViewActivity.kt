package com.example.sre_snickers

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sre_snickers.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.webView.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    binding.progressBar.progress = newProgress
                    if (newProgress == 100) {
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }

            val htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        * { box-sizing: border-box; margin: 0; padding: 0; }
                        body {
                            font-family: 'Poppins', sans-serif, Arial;
                            background-color: #1A0D08;
                            color: #FAEDCD;
                            padding: 16px;
                        }
                        .header {
                            background-image: linear-gradient(rgba(0,0,0,0.6), rgba(0,0,0,0.7)), url('backgroundsobremessa.jpeg');
                            background-size: cover;
                            background-position: center;
                            border-radius: 16px;
                            padding: 28px 16px;
                            text-align: center;
                            margin-bottom: 20px;
                            box-shadow: 0 4px 10px rgba(0,0,0,0.5);
                        }
                        .header h1 { font-size: 22px; color: #FAEDCD; font-weight: bold; }
                        .header p { font-size: 13px; color: #D4A373; margin-top: 6px; }
                        .section-title {
                            font-size: 18px;
                            font-weight: bold;
                            color: #FAEDCD;
                            margin-bottom: 12px;
                        }
                        .grid {
                            display: grid;
                            grid-template-columns: repeat(2, 1fr);
                            gap: 12px;
                        }
                        .card {
                            background: #2C1810;
                            border-radius: 12px;
                            overflow: hidden;
                            box-shadow: 0 3px 8px rgba(0,0,0,0.4);
                            display: flex;
                            flex-direction: column;
                        }
                        .card img {
                            width: 100%;
                            height: 120px;
                            object-fit: cover;
                        }
                        .card-body {
                            padding: 10px;
                            flex-grow: 1;
                            display: flex;
                            flex-direction: column;
                            justify-content: space-between;
                        }
                        .title { font-size: 13px; font-weight: bold; color: #FFFFFF; }
                        .category { font-size: 11px; color: #D4A373; margin-top: 2px; }
                        .price { font-size: 13px; font-weight: bold; color: #E07A5F; margin-top: 6px; }
                    </style>
                </head>
                <body>
                    <div class="header">
                        <h1>Sobremesa Bakery & Café</h1>
                        <p>Website Resmi & Catalogue Menu Indonesia</p>
                    </div>
                    <div class="section-title">Menu Utama & Favorit</div>
                    <div class="grid">
                        <div class="card">
                            <img src="beef.jpeg" alt="Beef Steak Croissant">
                            <div class="card-body">
                                <div>
                                    <div class="title">Beef Steak Croissant</div>
                                    <div class="category">Pastry</div>
                                </div>
                                <div class="price">Rp 45.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="cake.jpeg" alt="Strawberry Shortcake">
                            <div class="card-body">
                                <div>
                                    <div class="title">Strawberry Shortcake</div>
                                    <div class="category">Cake</div>
                                </div>
                                <div class="price">Rp 32.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="cake2.jpeg" alt="Tiramisu Delight Slice">
                            <div class="card-body">
                                <div>
                                    <div class="title">Tiramisu Delight Slice</div>
                                    <div class="category">Cake</div>
                                </div>
                                <div class="price">Rp 35.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="bread.jpeg" alt="Artisan Sourdough Toast">
                            <div class="card-body">
                                <div>
                                    <div class="title">Artisan Sourdough Toast</div>
                                    <div class="category">Pastry</div>
                                </div>
                                <div class="price">Rp 22.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="drink.jpeg" alt="Iced Milk Coffee Latte">
                            <div class="card-body">
                                <div>
                                    <div class="title">Iced Milk Coffee Latte</div>
                                    <div class="category">Coffee</div>
                                </div>
                                <div class="price">Rp 25.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="drink2.jpeg" alt="Matcha Latte Espresso">
                            <div class="card-body">
                                <div>
                                    <div class="title">Matcha Latte Espresso</div>
                                    <div class="category">Coffee</div>
                                </div>
                                <div class="price">Rp 28.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="drink3.jpeg" alt="Fresh Berry Tea Mocktail">
                            <div class="card-body">
                                <div>
                                    <div class="title">Fresh Berry Tea Mocktail</div>
                                    <div class="category">Coffee</div>
                                </div>
                                <div class="price">Rp 24.000</div>
                            </div>
                        </div>
                        <div class="card">
                            <img src="donut.jpeg" alt="Glazed Chocolate Donut">
                            <div class="card-body">
                                <div>
                                    <div class="title">Glazed Chocolate Donut</div>
                                    <div class="category">Pastry</div>
                                </div>
                                <div class="price">Rp 18.000</div>
                            </div>
                        </div>
                    </div>
                </body>
                </html>
            """.trimIndent()

            loadDataWithBaseURL("file:///android_res/drawable/", htmlContent, "text/html", "UTF-8", null)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }
}
