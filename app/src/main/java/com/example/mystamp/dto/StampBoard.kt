package com.example.mystamp.dto

import com.google.gson.annotations.SerializedName

data class StampBoard(
    @SerializedName("businessNumber")
    var businessNumber: String,

    @SerializedName("count")
    var stampCount: Int,

    var frontImage: String,

    var backImage: String,

    @SerializedName("stamp_limit")
    var maxCount: Int,

    var lineCount1: Int,

    var lineCount2: Int,

    var lineCount3: Int
){
    constructor(businessNumber: String) : this(businessNumber,0,"","",0,0,0,0)
}


