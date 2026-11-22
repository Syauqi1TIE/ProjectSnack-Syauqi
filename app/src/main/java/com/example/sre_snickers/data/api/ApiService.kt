package com.example.sre_snickers.data.api

import com.example.sre_snickers.data.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("rpc/register_user") suspend fun register(@Body body: RegisterRequest): AuthResponse
    @POST("rpc/login_user") suspend fun login(@Body body: LoginRequest): AuthResponse
    @POST("rpc/get_user") suspend fun getUser(@Body body: GetUserRequest): AuthResponse
    @POST("rpc/update_profile") suspend fun updateProfile(@Body body: UpdateProfileRequest): AuthResponse

    @GET("menu?aktif=eq.true&order=id.asc") suspend fun getMenu(): List<MenuModel>
    @GET("promo?aktif=eq.true&order=id.asc") suspend fun getPromo(): List<PromoModel>
    @GET("rewards?aktif=eq.true&order=poin.asc") suspend fun getRewards(): List<RewardModel>

    @GET("orders?select=*,order_items(*)&order=id.desc")
    suspend fun getOrders(@Query("user_id") userId: String): List<OrderModel>

    @POST("rpc/buat_pesanan") suspend fun checkout(@Body body: CheckoutRequest): CheckoutResponse
    @POST("rpc/tukar_poin") suspend fun tukarPoin(@Body body: RedeemRequest): RedeemResponse
}