package com.example.sre_snickers.util

import android.content.Context
import com.example.sre_snickers.data.model.UserModel

class SessionManager(context: Context) {
    private val sp = context.getSharedPreferences("user_pref", Context.MODE_PRIVATE)

    val userId: Long get() = sp.getLong("user_id", -1L)

    fun isLoggedIn(): Boolean = sp.getBoolean("isLogin", false) && userId != -1L

    fun saveUser(u: UserModel) {
        sp.edit()
            .putBoolean("isLogin", true)
            .putLong("user_id", u.id)
            .putString("nama", u.nama)
            .putString("email", u.email)
            .putString("tanggal_lahir", u.tanggal_lahir ?: "")
            .putString("jenis_kelamin", u.jenis_kelamin ?: "")
            .putString("username", u.username)
            .putString("current_username", u.username)
            .putInt("member_points", u.poin)
            .apply()
    }

    fun updatePoints(poin: Int) { sp.edit().putInt("member_points", poin).apply() }

    fun logout() { sp.edit().clear().apply() }
}