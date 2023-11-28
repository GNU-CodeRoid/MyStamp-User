package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class ShopData(
    @SerializedName("shop_id")
    val shopId: ShopId,
    val count: Int
)