package com.example.mystamp.viewmodel

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystamp.AppManager
import com.example.mystamp.R
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

class MainViewModel : ViewModel() {
    private val serverConnectHelper = ServerConnectHelper()

    private var _stampBoards by mutableStateOf<List<StampBoard>>(emptyList())
    private var _currentPage by mutableIntStateOf(0)
    private var _fetchTrigger by mutableStateOf(false)

    @OptIn(ExperimentalPagerApi::class)
    private lateinit var _pagerState: PagerState


    val stampBoards: List<StampBoard> get() = _stampBoards
    val currentPage: Int get() = _currentPage

    val fetchTrigger: Boolean get() = _fetchTrigger

    @OptIn(ExperimentalPagerApi::class)
    val pagerState: PagerState get() = _pagerState


    @OptIn(ExperimentalPagerApi::class)
    fun updatePagerState(pagerState: PagerState){
        _pagerState = pagerState
    }

    fun updateFetchTrigger(trigger: Boolean){
        _fetchTrigger = trigger
    }
    fun updatePage(page: Int){
        _currentPage = page
    }
    fun fetchStampBoards() {
        viewModelScope.launch {
            try {
                val data: List<ShopData> = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine { continuation ->
                        serverConnectHelper.requestStampBoards =
                            object : ServerConnectHelper.RequestStampBoards {
                                override fun onSuccess(data: List<ShopData>) {
                                    continuation.resume(data)
                                }

                                override fun onFailure() {
                                    continuation.resume(emptyList())
                                }
                            }
                        serverConnectHelper.getStampBoards(AppManager.getUid()!!)
                    }
                }

                _stampBoards = if (data.isNotEmpty()) {
                    data.map { datum ->
                        StampBoard().apply {
                            businessNumber = datum.shopId.businessNumber
                            shopName = datum.shopId.shopName
                            frontImage = datum.image
                            backImage =
                                "https://postfiles.pstatic.net/MjAyMzEyMDZfMjc0/MDAxNzAxODY3MzMyNTMw.MswJ_PgbXwQaB2Lf20a7rAS2QBsiuVTgTSg2z91Xeksg.nghKgJ01YGvV2XqNUxMKWB7zDFBEGFmGNlLKwq01EQ4g.PNG.cha_dh1004/002.png?type=w966"
                            stampCount = datum.count
                            maxCount = datum.shopId.stampLimit

                            Log.d("test", "사업자번호: ${datum.image}")
                        }
                    } + StampBoard("last","") // 추가 데이터
                } else {
                    listOf(StampBoard("last","")) // 기본 데이터만 반환
                }
            } catch (e: Exception) {
                // Handle the exception (log, report, etc.)
                Log.e("error", "Error fetching stamp boards", e)
            }
        }
    }
}