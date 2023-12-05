package com.example.mystamp.dto

data class RequestUserData(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val stamps: List<StampBoard>,
    val coupons: List<Coupon>,
    val createTime: String,
    val updateTime: String
){
    constructor() : this("0","없음","010-0000-0000", emptyList(), emptyList(),"없음","없음")
}
