
/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mystamp.activity
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.mystamp.AppManager
import com.example.mystamp.R
import com.example.mystamp.dto.RequestAddStampData
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.utils.QRHelper
import com.example.mystamp.utils.ServerConnectHelper
import com.example.mystamp.viewmodel.MainViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {


    private val serverConnectHelper = ServerConnectHelper()
    private val mainViewModel = MainViewModel()
    private lateinit var qrHelper: QRHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        qrCodeInit()
        setContent {
            MyStampTheme{
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Screen()
                }
            }
        }

    }


    private fun qrCodeInit(){
        qrHelper = QRHelper(this){ scannedContent ->

            serverConnectHelper.requestAddStamp = object : ServerConnectHelper.RequestAddStamp{
                override fun onSuccess(message: String) {
                    Log.d("test",message)
                    Toast.makeText(this@MainActivity, "스탬프 적립", Toast.LENGTH_SHORT).show()
                    mainViewModel.stampBoards[mainViewModel.currentPage].stampCount += 1
                    mainViewModel.updateFetchTrigger(!mainViewModel.fetchTrigger)

                }

                override fun onFailure() {
                    Toast.makeText(this@MainActivity, "스탬프 적립 오류.", Toast.LENGTH_SHORT).show()
                }

            }

            val requestAddStampData = RequestAddStampData(scannedContent,AppManager.getUid()!!)
            serverConnectHelper.addStamp(requestAddStampData)

        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Screen() {
        MyStampTheme {
            val activity = LocalContext.current as? Activity
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(
                            text = stringResource(R.string.app_name)
                        ) },

                        actions = {
                            Row(
                                modifier = Modifier.padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                IconButton(
                                    onClick = {
                                        if (activity != null) {
                                            toCouponActivity(activity)
                                        }
                                    },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Filled.CreditCard,
                                            contentDescription = "쿠폰"
                                        )
                                    }
                                )
                                IconButton(
                                    onClick = {
                                        if (activity != null) {
                                            toUserActivity(activity)
                                        }
                                    },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = "정보"
                                        )
                                    }
                                )
                            }

                        }
                    )
                },
                modifier = Modifier.fillMaxSize()
            ) { padding ->
                if (activity != null) {
                    HorizontalPagerWithOffsetTransition(Modifier.padding(padding))
                }
            }
        }

    }



    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
        // 현재 페이지를 기록하기 위한 pagerState
        mainViewModel.updatePagerState(rememberPagerState())
        val pagerState = mainViewModel.pagerState

        // 현재 페이지를 추적하는 MutableState
        val currentPage = mainViewModel.pagerState.currentPage
        val stampBoards = mainViewModel.stampBoards
        val fetchStampBoardsTrigger = mainViewModel.fetchTrigger

        // 다이얼로그 표시 상태를 관리하는 MutableState
        var showDialog by remember { mutableStateOf(false) }


        LaunchedEffect(fetchStampBoardsTrigger) {
            try {
                mainViewModel.fetchStampBoards()

                Log.d("StampBoards", "스탬프 보드 크기: ${stampBoards.size}")
            } catch (e: Exception) {
                Log.e("NetworkError", "스탬프 보드 가져오기 오류", e)
            }
        }

        // pagerState의 현재 페이지를 감시하고 currentPage를 업데이트
        LaunchedEffect(currentPage) {
            mainViewModel.updatePage(currentPage)
        }

        Column(
            modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // 카드 윗 부분에 포커스된 페이지 인덱스에 따라 점을 그리기
            Row( // 윗 부분에 공백 추가
                horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                for (index in stampBoards.indices) {
                    val isCurrentPage = index == currentPage
                    val color = if (isCurrentPage) Color.Gray else Color.LightGray
                    val size = if (isCurrentPage) 12.dp else 8.dp
                    Box(
                        modifier = Modifier
                            .size(size)
                            .background(color, CircleShape),

                        )
                }
            }

            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(200.dp),
                state = pagerState,
                count = stampBoards.size,
                contentPadding = PaddingValues(horizontal = 16.dp),

                ) { page ->


                Card(
                    Modifier
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            Log.d(
                                "PagerState",
                                "Current page: ${currentPage}, Scroll offset: ${mainViewModel.pagerState.currentPageOffset}"
                            )

                            // We animate the scaleX + scaleY, between 85% and 100%
                            lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            ).also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                            // We animate the alpha, between 50% and 100%
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                        .fillMaxWidth()
                        .aspectRatio(9 / 5f)
                        .padding(top = 30.dp)
                        .clickable {
                            Log.d("ClickEvent", "Click")
                            //마지막 스탬프보드(+버튼이 있는 이미지) 스탬프 보드 추가에 사용
                            if (page == stampBoards.size - 1) {
                                qrHelper.scanQRCode()
                            } else {
                                // 이미지 카드 클릭 Dialog 동작
                                showDialog = true

                            }


                        }

                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box {

                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = rememberImagePainter(
                                    data = when (stampBoards[page].shopName) {
                                        "last" -> R.drawable.blank
                                        else -> stampBoards[page].frontImage
                                    }
                                ),
                                contentDescription = "스탬프 보드 메인",
                                contentScale = ContentScale.Crop    // 이미지가 잘려서 확대됩니다.

                            )

                        }
                    }

                }

            }


            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(30.dp))

            IconButton(
                onClick = {
                    qrHelper.scanQRCode()
                    showDialog = false // 대화 상자를 닫기 위해 showDialog를 false로 설정합니다.
                }) {
                    Icon(
                        modifier = Modifier.size(150.dp),
                        imageVector = Icons.Filled.QrCode,
                        contentDescription = "qr코드 스캔")

            }

        }



        if (showDialog) { // 대화 상자를 표시해야 하는지 확인합니다.
            Dialog(onDismissRequest = { showDialog = false }) { // 닫기 기능이 있는 대화 상자를 만듭니다.
                Box( // 대화 상자 내용을 담는 컨테이너입니다.
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column( // 대화 상자 내용의 열 레이아웃입니다.
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                        Text(
                            fontSize = 30.sp,
                            text=stampBoards[currentPage].shopName
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                        Box {
                            Image( // 이미지를 표시합니다.
                                painter = rememberAsyncImagePainter(model = stampBoards[currentPage].backImage),
                                contentDescription = "스탬프 보드 내용",
                                modifier = Modifier.fillMaxWidth(),
                                contentScale = ContentScale.Crop
                            )
                            DrawStampLines(stampBoards[currentPage].stampCount, 20, 15)
                        }

                        Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                        Button( // QR 코드를 스캔하기 위한 버튼입니다.
                            onClick = {


                                serverConnectHelper.requestDeleteStamp = object : ServerConnectHelper.RequestDeleteStamp{
                                    override fun onSuccess(message: String) {
                                        mainViewModel.updateFetchTrigger(!fetchStampBoardsTrigger)
                                        showDialog = false
                                        Log.d("test","삭제 성공")
                                    }

                                    override fun onFailure() {
                                        Log.d("test","삭제 실패")
                                    }

                                }

                                serverConnectHelper.deleteStamp(AppManager.getUid()!!,stampBoards[currentPage].businessNumber)





                            },
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .heightIn(max = 50.dp)
                        ) {
                            Text( // 버튼에 표시되는 텍스트입니다.
                                "스탬프 보드 삭제"
                            )

                        }

                    }
                }
                Log.d("test",stampBoards[currentPage].stampCount.toString())
                // 수에 따라 스탬프를 표시합니다.





            }
        }


    }


    @Composable
    private fun DrawStampLines(stampCount: Int, paddingTop: Int, paddingStart: Int) {
        val stampLineCounts = listOf(5, 10,15) // Define the stamp count thresholds for each line
        val lineCount = 5
        stampLineCounts.forEachIndexed { index, lineThreshold ->
            if (stampCount >= lineThreshold) {

                val topPadding = paddingTop + index * 45 // Adjust the padding based on your requirements
                Stamping(lineCount, topPadding, paddingStart)
            }else{
                val checkLineCount = lineCount - (lineThreshold - stampCount)
                if(lineCount > checkLineCount){
                    val topPadding = paddingTop + index * 45 // Adjust the padding based on your requirements
                    Stamping(checkLineCount, topPadding, paddingStart)
                }
            }
        }
    }

    @Composable
    private fun Stamping(lineCount: Int,paddingTop: Int ,paddingStart: Int) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(top = paddingTop.dp, start = paddingStart.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)// 가로 정렬 방식을 설정합니다. 각 요소 사이에 2dp 간격을 두고 배치합니다.
        ) {
            repeat(lineCount) {
                Image(
                    painter = painterResource(R.drawable.stamp_image),
                    contentDescription = null,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
    }

    private fun toCouponActivity(activity: Activity){
        val intent = Intent(activity, CouponActivity::class.java)
        startActivity(intent)
    }

    private fun toUserActivity(activity: Activity){
        val intent = Intent(activity, UserActivity::class.java)
        startActivity(intent)
    }

}




