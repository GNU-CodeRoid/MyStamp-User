package com.example.mystamp.viewmodel

import android.service.autofill.UserData
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystamp.AppManager
import com.example.mystamp.dto.RequestUserData
import com.example.mystamp.dto.ShopData
import com.example.mystamp.dto.StampBoard
import com.example.mystamp.utils.ServerConnectHelper
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class UserViewModel : ViewModel() {
    private val serverConnectHelper = ServerConnectHelper()

    private var _fetchTrigger by mutableStateOf(false)
    private var _userData by mutableStateOf(RequestUserData())

    @OptIn(ExperimentalPagerApi::class)
    private lateinit var _pagerState: PagerState


    val userData: RequestUserData get() = _userData

    val fetchTrigger: Boolean get() = _fetchTrigger

    fun updateFetchTrigger(trigger: Boolean){
        _fetchTrigger = trigger
    }

    fun fetchUserData() {
        viewModelScope.launch {
            try {
                val data: RequestUserData = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine { continuation ->
                        serverConnectHelper.userDataRequest =
                            object : ServerConnectHelper.UserDataRequest {

                                override fun onSuccess(userData: RequestUserData) {
                                    continuation.resume(userData)
                                    Log.d("test","통신성공")
                                }

                                override fun onFailure() {
                                    continuation.resume(RequestUserData())
                                    Log.d("test","통신실패")
                                }
                            }
                        serverConnectHelper.getUserData(AppManager.getUid()!!)
                    }
                }
                _userData = data

            } catch (e: Exception) {
                // Handle the exception (log, report, etc.)
                Log.e("error", "Error fetching userData", e)
            }
        }
    }
}