package com.example.mystamp.viewmodel

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystamp.AppManager
import com.example.mystamp.dto.Coupon
import com.example.mystamp.dto.StampBoard
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class CouponViewModel : ViewModel() {
    private val serverConnectHelper = ServerConnectHelper()

    private var _coupons by mutableStateOf<List<Coupon>>(emptyList())
    private var _isFetchTrigger by mutableStateOf(false)
    private var _currentIndex by mutableIntStateOf(0)
    private var _isShowDialog by mutableStateOf(false)
    private var _isShowCheckDialog by mutableStateOf(false)
    val coupons: List<Coupon> get() = _coupons

    val currentIndex: Int get() = _currentIndex


    val isFetchTrigger: Boolean get() = _isFetchTrigger

    val isShowDialog: Boolean get() = _isShowDialog

    val isShowCheckDialog: Boolean get() = _isShowCheckDialog

    fun showDialog(index: Int){
        updateCurrentIndex(index)
        _isShowDialog = true
    }

    fun closeDialog(){
        _isShowDialog = false
    }

    fun showCheckDialog(){
        _isShowCheckDialog = true
    }

    fun closeCheckDialog(){
        _isShowCheckDialog = false
    }


    fun updateFetchTrigger(trigger: Boolean){
        _isFetchTrigger = trigger
    }

    private fun updateCurrentIndex(index: Int){
        _currentIndex = index
    }

    fun fetchCoupons() {
        viewModelScope.launch {
            try {
                val data: List<Coupon> = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine { continuation ->
                        serverConnectHelper.couponsRequest =
                            object : ServerConnectHelper.CouponsRequest{
                                override fun onSuccess(coupons: List<Coupon>) {
                                    continuation.resume(coupons)
                                    Log.d("test", " fetch 성공")
                                }

                                override fun onFailure() {
                                    Log.d("test", " fetch 실패")
                                    continuation.resume(emptyList())
                                }
                            }
                        serverConnectHelper.getCoupons(AppManager.getUid()!!)
                    }
                }
                _coupons = data


            } catch (e: Exception) {
                // Handle the exception (log, report, etc.)
                Log.e("error", "Error fetching coupon", e)
            }
        }
    }

    fun deleteCoupon() {
            serverConnectHelper.couponsDeleteRequest =
                object : ServerConnectHelper.CouponsDeleteRequest {
                    override fun onSuccess(message: String) {
                        updateFetchTrigger(!isFetchTrigger)
                        Log.d("test", "삭제 성공")
                    }

                    override fun onFailure() {
                        Log.d("test", "삭제 실패")
                    }

                }

            serverConnectHelper.deleteCoupon(
                AppManager.getUid()!!,
                coupons[currentIndex].couponCode
            )
        closeDialog()
    }
}