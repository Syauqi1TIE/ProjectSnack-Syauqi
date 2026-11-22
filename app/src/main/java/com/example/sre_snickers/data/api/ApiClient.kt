package com.example.sre_snickers.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // Contoh: "https://abcdxyz.supabase.co/rest/v1/"  (wajib diakhiri tanda /)
    private const val BASE_URL = "https://ISI-PROJECT-ANDA.supabase.co/rest/v1/"
    private const val API_KEY = "ISI-PUBLISHABLE-KEY-ANDA"
    private const val SCHEMA = "mobile"

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val req = chain.request()
            val builder = req.newBuilder().addHeader("apikey", API_KEY)
            // GET pakai Accept-Profile, selain GET pakai Content-Profile (seperti di modul)
            if (req.method == "GET") builder.addHeader("Accept-Profile", SCHEMA)
            else builder.addHeader("Content-Profile", SCHEMA)
            chain.proceed(builder.build())
        }
        .build()

    val service: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}