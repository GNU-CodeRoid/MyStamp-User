package com.example.mystamp.dto
import com.google.gson.annotations.SerializedName

class Coupon(
    @SerializedName("couponCode")
    val couponCode: String,
    @SerializedName("shop_id")
    val shopId: ShopId
)
