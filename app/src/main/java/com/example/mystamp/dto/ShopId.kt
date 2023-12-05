package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class ShopId(
    @SerializedName("shop_id")
    val shopId: String,
    @SerializedName("shop_name")
    val shopName: String,
    @SerializedName("stamp_limit")
    val stampLimit: Int,
    @SerializedName("businessNumber")
    val businessNumber: String,
    @SerializedName("coupon_category")
    val couponCategory: String,
    @SerializedName("coupon_description")
    val couponDescription: String,
    @SerializedName("createTime")
    val createTime: String,
    @SerializedName("updateTime")
    val updateTime: String
)