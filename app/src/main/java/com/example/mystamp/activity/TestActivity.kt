
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
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.viewinterop.AndroidView
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mystamp.R
import com.example.mystamp.ui.theme.MyStampTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentIntegrator.parseActivityResult
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import kotlin.math.absoluteValue
import com.journeyapps.barcodescanner.CaptureActivity

class AnyOrientationCaptureActivity : CaptureActivity() {
    //QR 코드 스캐너 화면 회전 관련
}

class TestActivity : ComponentActivity() {


    private val sampleImageUrls = listOf(
        // 다른 이미지 URL을 추가하세요.
        "https://wemix-dev-s3.s3.amazonaws.com/media/sample/%EC%BF%A0%ED%8F%B0(%EB%AA%85%ED%95%A8)/2019/NC242B.jpg",
        "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg",
        "https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=13221193&filePath=L2Rpc2sxL25ld2RhdGEvMjAxOS8yMS9DTFMxMDAwNC8xMzIyMTE5M19XUlRfMjAxOTExMjFfMQ==&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10004",
        "https://www.adobe.com/kr/creativecloud/photography/hub/guides/media_197108eb1be1977ee2a87738b4cd58efbf34f5ba7.jpeg?width=750&format=jpeg&optimize=medium",
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyStampTheme {
                Surface {
                    Sample()

                }
            }
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun Sample() {

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                // QR 코드 데이터는 result.contents에 저장됩니다.
                val qrCodeData = result.contents
                // QR 코드 데이터를 Toast 메시지로 표시합니다.
                Toast.makeText(this, "스캔 완료 : $qrCodeData", Toast.LENGTH_SHORT).show()
                // QR 코드 데이터를 Logcat에 로깅합니다.
                Log.d("QRCodeScan", "스캔한 QR 코드 데이터: $qrCodeData")
                // QR 코드로 스캔된 링크를 열기 위해 Intent를 생성합니다.
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(qrCodeData))
                // 생성한 Intent를 실행하여 링크를 엽니다.
                startActivity(intent)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    // QR 코드 스캐너 실행 함수
    private fun scanQRCode() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("QR Code Scanner")
        integrator.captureActivity = AnyOrientationCaptureActivity::class.java  // //QR 코드 스캐너 회전 관련
        integrator.setOrientationLocked(true) // 스캔 화면 세로 고정
        integrator.setBeepEnabled(false)    //  스캔 시 소리 끔
        integrator.initiateScan()
    }



    @OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
    @Composable
    fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
        val pageCount = sampleImageUrls.size // 이미지 리스트의 사이즈 (이 경우에는 4)

        // 현재 페이지를 기록하기 위한 pagerState
        val pagerState = rememberPagerState()

        // 현재 페이지를 추적하는 MutableState
        var currentPage by remember { mutableStateOf(0) }

        // pagerState의 현재 페이지를 감시하고 currentPage를 업데이트
        LaunchedEffect(pagerState.currentPage) {
            currentPage = pagerState.currentPage
        }
        HorizontalPager(
            state = pagerState,
            count = pageCount,
            // Add 32.dp horizontal padding to 'center' the pages
            contentPadding = PaddingValues(horizontal = 60.dp),
            modifier = modifier.fillMaxSize()
        ) { page ->
            Card(
                Modifier
                    .graphicsLayer {
                        // Calculate the absolute offset for the current page from the
                        // scroll position. We use the absolute value which allows us to mirror
                        // any effects for both directions
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue

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
                    //.aspectRatio(1f)
                    .aspectRatio(9/5f)
                    .clickable {
                        Log.d("ClickEvent", "Click")
                        scanQRCode()

                    }, border = BorderStroke(1.dp, Color.LightGray), // 테두리 추가

            ) {
                Column(modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {

                        Image(
                            painter = rememberImagePainter(data = sampleImageUrls[page]),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.Crop    // 이미지가 잘려서 확대됩니다.

                        )

                    }
                }

            }
        }
        // 카드 윗 부분에 포커스된 페이지 인덱스에 따라 점을 그리기
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 280.dp), // 윗 부분에 공백 추가
            horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
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





/*    @OptIn(ExperimentalCoilApi::class)
    @Composable
    private fun ProfilePicture(modifier: Modifier = Modifier) {
        Card(
            modifier = modifier,
            shape = CircleShape,
            border = BorderStroke(4.dp, MaterialTheme.colors.surface)
        ) {
            Image(
                painter = rememberImagePainter(data=rememberRandomSampleImageUrl()),
                contentDescription = null,
                modifier = Modifier.size(72.dp),
            )
        }
    }*/