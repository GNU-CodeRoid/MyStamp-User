package com.example.mystamp.activity

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.mystamp.dto.ShopData
import com.example.mystamp.dto.StampBoard
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class MainViewModel : ViewModel() {
    private val _data = mutableStateOf("data")
    private val serverConnectHelper = ServerConnectHelper()

    val data: State<String> get () = _data

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun fetchStampBoards(): List<StampBoard> {
        val basicStampBoard = StampBoard(businessNumber = "last")
        return try {

            val data: List<ShopData> = withContext(Dispatchers.IO) {
                suspendCancellableCoroutine { continuation ->
                    serverConnectHelper.requestStampBoards = object : ServerConnectHelper.RequestStampBoards {
                        override fun onSuccess(data: List<ShopData>) {
                            continuation.resume(data) {
                                // This block will be executed if the coroutine is cancelled.
                                // Clean up resources or handle cancellation if needed.
                            }
                        }

                        override fun onFailure() {
                            continuation.resume(emptyList())
                        }
                    }
                    serverConnectHelper.getStampBoards("01099716737")
                }
            }

            val stampBoards = data.map { datum ->
                StampBoard("last").apply {
                    frontImage = "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2023/NC214F.jpg"
                    backImage = "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2023/NC214B.jpg"
                    stampCount = datum.count
                    businessNumber = datum.shopId.businessNumber
                    maxCount = datum.shopId.stampLimit

                    Log.d("test","사업자번호: ${datum.shopId.businessNumber}")
                }

            }

            // 통신이 성공하든 실패하든 비어있다면 기본데이터 추가
            if (stampBoards.isEmpty()) {
                return listOf(basicStampBoard)
            }

            // 기본 데이터와 통신에서 받은 데이터를 함께 반환
            return listOf(basicStampBoard) + stampBoards

        } catch (e: Exception) {
            // Handle the exception (log, report, etc.)
            listOf(basicStampBoard)
        }
    }
}