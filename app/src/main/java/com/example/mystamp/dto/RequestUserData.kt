package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class RequestUserData(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val stamps: List<StampBoard>,
    val coupons: List<Coupon>,
    val createTime: String,
    val updateTime: String
)
