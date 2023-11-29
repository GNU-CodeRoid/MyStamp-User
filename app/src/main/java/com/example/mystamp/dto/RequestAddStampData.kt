package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class RequestAddStampData(
    @SerializedName("businessNumber")
    val businessNumber: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String)
