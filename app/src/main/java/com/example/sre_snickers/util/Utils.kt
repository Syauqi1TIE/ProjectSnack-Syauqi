package com.example.sre_snickers.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.sre_snickers.R
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.coroutines.cancellation.CancellationException

fun Int.toRupiah(): String = "Rp " + String.format(Locale.forLanguageTag("id-ID"), "%,d", this)

/** Kalau isi kolom gambar berupa URL -> pakai Glide. Kalau nama file -> ambil dari res/drawable. */
fun ImageView.loadMenuImage(name: String?, fallback: Int = R.drawable.sobremessa) {
    if (name.isNullOrBlank()) { setImageResource(fallback); return }
    if (name.startsWith("http")) {
        Glide.with(context).load(name).placeholder(fallback).error(fallback).into(this)
    } else {
        val id = resources.getIdentifier(name, "drawable", context.packageName)
        setImageResource(if (id != 0) id else fallback)
    }
}

suspend fun <T> apiCall(block: suspend () -> T): Result<T> =
    try { Result.success(block()) }
    catch (e: CancellationException) { throw e }
    catch (e: Exception) { Result.failure(e) }

fun Throwable.friendlyMessage(): String = when (this) {
    is UnknownHostException, is ConnectException -> "Tidak ada koneksi internet"
    is SocketTimeoutException -> "Koneksi terlalu lama, coba lagi"
    is HttpException -> "Server bermasalah (kode ${code()})"
    else -> "Terjadi kesalahan: ${message}"
}

fun formatTanggal(iso: String): String = try {
    OffsetDateTime.parse(iso).atZoneSameInstant(ZoneId.of("Asia/Jakarta"))
        .format(DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm", Locale.forLanguageTag("id-ID")))
} catch (e: Exception) { iso }