package com.example.mystamp.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystamp.AppManager
import com.example.mystamp.dto.Coupon
import com.example.mystamp.dto.RequestUserData
import com.example.mystamp.dto.ShopData
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class CouponViewModel : ViewModel() {
    private val serverConnectHelper = ServerConnectHelper()

    private var _coupons by mutableStateOf<List<Coupon>>(emptyList())
    private var _fetchTrigger by mutableStateOf(false)

    val coupons: List<Coupon> get() = _coupons

    val fetchTrigger: Boolean get() = _fetchTrigger

    fun updateFetchTrigger(trigger: Boolean){
        _fetchTrigger = trigger
    }

    fun fetchCoupons() {
        viewModelScope.launch {
            try {
                val data: RequestUserData = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine { continuation ->
                        serverConnectHelper.userDataRequest =
                            object : ServerConnectHelper.UserDataRequest{
                                override fun onSuccess(userData: RequestUserData) {
                                    continuation.resume(userData)
                                }

                                override fun onFailure() {
                                    Log.d("test", " fetch 실패")
                                }
                            }
                        serverConnectHelper.getUserData(AppManager.getUid()!!)
                    }
                }

                _coupons = data.coupons


            } catch (e: Exception) {
                // Handle the exception (log, report, etc.)
                Log.e("error", "Error fetching coupon", e)
            }
        }
    }
}