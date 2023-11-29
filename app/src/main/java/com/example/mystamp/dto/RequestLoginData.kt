package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class RequestLoginData(
    @SerializedName("phoneNumber")
    val phoneNumber: String,

)