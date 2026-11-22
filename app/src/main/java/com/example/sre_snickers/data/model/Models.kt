package com.example.sre_snickers.data.model

data class UserModel(
    val id: Long, val nama: String, val email: String,
    val tanggal_lahir: String?, val jenis_kelamin: String?,
    val username: String, val poin: Int
)
data class AuthResponse(val ok: Boolean, val pesan: String, val user: UserModel? = null, val id: Long? = null)

data class MenuModel(val id: Long, val nama: String, val harga: Int, val kategori: String, val gambar: String?)
data class PromoModel(val id: Long, val tag: String, val judul: String, val subjudul: String?, val gambar: String?)
data class RewardModel(val id: Long, val nama: String, val poin: Int, val gambar: String?)

data class OrderItemModel(val menu_id: Long?, val nama: String, val harga: Int, val qty: Int)
data class OrderModel(
    val id: Long, val total: Int, val poin_didapat: Int, val status: String,
    val created_at: String, val order_items: List<OrderItemModel>
)
data class CheckoutResponse(
    val ok: Boolean, val pesan: String, val order_id: Long?,
    val total: Int?, val poin_didapat: Int?, val poin_baru: Int?
)
data class RedeemResponse(val ok: Boolean, val pesan: String, val kode: String?, val poin_baru: Int?)

// ===== Body request (nama field harus sama dengan nama parameter fungsi SQL) =====
data class RegisterRequest(
    val p_nama: String, val p_email: String, val p_tanggal_lahir: String,
    val p_jenis_kelamin: String, val p_username: String, val p_password: String
)
data class LoginRequest(val p_username: String, val p_password: String)
data class GetUserRequest(val p_id: Long)
data class UpdateProfileRequest(
    val p_id: Long, val p_nama: String, val p_tanggal_lahir: String,
    val p_jenis_kelamin: String, val p_username: String, val p_password: String
)
data class CartLine(val menu_id: Long, val qty: Int)
data class CheckoutRequest(val p_user_id: Long, val p_items: List<CartLine>)
data class RedeemRequest(val p_user_id: Long, val p_reward_id: Long)