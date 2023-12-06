package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class ShopData(
    @SerializedName("shopId")
    val shopId: ShopId,
    val image: String,
    val count: Int
)