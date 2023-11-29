package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class RequestRegisterData(
    @SerializedName("name")
    val name: String,
    @SerializedName("phoneNumber")
    val phoneNumber: String,

)