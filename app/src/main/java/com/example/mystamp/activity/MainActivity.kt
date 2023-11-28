
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

@file:Suppress("DEPRECATION")
package com.example.mystamp.activity
import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import com.example.mystamp.utils.QRHelper
import com.example.mystamp.R
import com.example.mystamp.dto.StampBoard
import com.example.mystamp.ui.theme.MyStampTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.zxing.integration.android.IntentIntegrator.parseActivityResult
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {

    private val frontImageUrls = listOf(
        // 다른 이미지 URL을 추가하세요.
        "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2023/NC214F.jpg",
        "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2023/NC213F.jpg",
        "last"
    )

    private val backImageUrls = listOf(
        // 다른 이미지 URL을 추가하세요.
        "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2023/NC214B.jpg",
        "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2023/NC213B.jpg",
        "last"
    )

    private var scanComplete = false
    private var qrCodeData: String? = null

    private lateinit var stampBoards: List<StampBoard>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            stampBoards = frontImageUrls.map { StampBoard(stampCount = 0, image = it, 15,0,0,0) }
            MyStampTheme {
                Surface {
                    Screen()
                }
            }
        }
    }


    override fun onDestroy() {
        qrCodeData = null // Reset qrCodeData to null or an appropriate default value
        super.onDestroy()
    }


    @Composable
    private fun Screen() {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    backgroundColor = MaterialTheme.colors.surface,
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { padding ->
            HorizontalPagerWithOffsetTransition(Modifier.padding(padding))
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // QR 코드 데이터는 result.contents에 저장됩니다.
                qrCodeData = result.contents
                // QR 코드 데이터를 Toast 메시지로 표시합니다.
                // QR 코드 데이터를 Logcat에 로깅합니다.
                Log.d("QRCodeScan", "스캔한 QR 코드 데이터: $qrCodeData")

                scanComplete = true


                // QR 코드로 스캔된 링크를 열기 위해 Intent를 생성합니다.
                //val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrCodeData))
                // 생성한 Intent를 실행하여 링크를 엽니다.
                //startActivity(intent)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }






    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
        val pageCount = frontImageUrls.size // 이미지 리스트의 사이즈 (이 경우에는 4)
        val currentActivity = LocalContext.current as? Activity
        // 현재 페이지를 기록하기 위한 pagerState
        val pagerState = rememberPagerState()

        // 현재 페이지를 추적하는 MutableState
        var currentPage by remember { mutableIntStateOf(0) }

        // pagerState의 현재 페이지를 감시하고 currentPage를 업데이트
        LaunchedEffect(pagerState.currentPage) {
            currentPage = pagerState.currentPage
            pagerState.animateScrollToPage(pagerState.currentPage, /* your animation specs here */)
        }

        // 다이얼로그 표시 상태를 관리하는 MutableState
        var showDialog by remember { mutableStateOf(false) }


        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HorizontalPager(
                state = pagerState,
                count = pageCount,
                contentPadding = PaddingValues(horizontal = 16.dp),

                ) { page ->

                Card(
                    Modifier
                        .graphicsLayer {
                            // Calculate the absolute offset for the current page from the
                            // scroll position. We use the absolute value which allows us to mirror
                            // any effects for both directions
                            val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

                            Log.d("PagerState", "Current page: ${pagerState.currentPage}, Scroll offset: ${pagerState.currentPageOffset}")

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
                            if (page == (pageCount - 1)) {
                                Toast
                                    .makeText(currentActivity, "마지막 스탬프보드 입니다", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                // 이미지 카드 클릭 Dialog 동작
                                showDialog = true


                            }


                        }.background(Color.Red) // 임시 배경색, border = BorderStroke(1.dp, Color.LightGray), // 테두리 추가

                ) {
                    Column(
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Box {

                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = rememberImagePainter(
                                    data = when {
                                        frontImageUrls[page] == "last" -> R.drawable.blank
                                        else -> frontImageUrls[page]
                                    }
                                ),
                                contentDescription = "스탬프 보드 메인",
                                contentScale = ContentScale.Crop    // 이미지가 잘려서 확대됩니다.

                            )

                        }
                    }

                }

                // 카드 윗 부분에 포커스된 페이지 인덱스에 따라 점을 그리기
                Row( // 윗 부분에 공백 추가
                    horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    for (index in 0 until pageCount) {
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

            }
        }


        if (showDialog) { // 대화 상자를 표시해야 하는지 확인합니다.
            Dialog(onDismissRequest = { showDialog = false }) { // 닫기 기능이 있는 대화 상자를 만듭니다.
                Box( // 대화 상자 내용을 담는 컨테이너입니다.
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp)
                ) {
                    Column( // 대화 상자 내용의 열 레이아웃입니다.
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image( // 이미지를 표시합니다.
                            painter = rememberImagePainter(data = backImageUrls[currentPage]),
                            contentDescription = "스탬프 보드 내용",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp)) // 요소 사이에 여백을 추가합니다.
                        Button( // QR 코드를 스캔하기 위한 버튼입니다.
                            onClick = {
                                if (currentActivity != null) { // currentActivity가 null이 아닌지 확인합니다.
                                    QRHelper.scanQRCode(currentActivity) // QR 코드를 스캔하는 함수를 호출합니다.
                                }
                                showDialog = false // 대화 상자를 닫기 위해 showDialog를 false로 설정합니다.

                            },
                            modifier = Modifier
                                .widthIn(max = 200.dp)
                                .heightIn(max = 50.dp)
                        ) {
                            Text( // 버튼에 표시되는 텍스트입니다.
                                "QR코드 스캔",
                                color = Color.White // 텍스트 색상을 흰색으로 설정합니다.
                            )
                        }
                    }
                }

                if (scanComplete) { // 스캔이 완료되었는지 확인합니다.
                    // 스탬프 수를 증가시키고 조건에 따라 디스플레이를 업데이트합니다.


                    if(stampBoards[currentPage].stampCount == stampBoards[currentPage].maxCount){
                        Toast.makeText(this, "스탬프를 모두 채웠습니다", Toast.LENGTH_SHORT).show()
                    }else{
                        stampBoards[currentPage].stampCount += 1
                        Toast.makeText(this, "스탬프가 적립되었습니다.", Toast.LENGTH_SHORT).show()
                    }

                    when {
                        stampBoards[currentPage].stampCount < 5 -> {
                            stampBoards[currentPage].lineCount1 += 1
                        }
                        stampBoards[currentPage].stampCount < 10 -> {
                            stampBoards[currentPage].lineCount2 += 1
                        }
                        stampBoards[currentPage].stampCount < 15 -> {
                            stampBoards[currentPage].lineCount3 += 1
                        }
                        else -> {

                        }
                    }
                }


                // 수에 따라 스탬프를 표시합니다.

                Stamping(stampBoards[currentPage].lineCount1,35,30)
                if (stampBoards[currentPage].stampCount > 5) { // 스탬프 수가 5를 초과하는지 확인합니다.
                    Stamping(stampBoards[currentPage].lineCount2,80,30)
                }
                if (stampBoards[currentPage].stampCount > 10) {
                    Stamping(stampBoards[currentPage].lineCount3,125,30)
                }

                scanComplete = false // 다음 반복을 위해 scanComplete 플래그를 재설정합니다.
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

}




