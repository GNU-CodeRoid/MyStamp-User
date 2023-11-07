
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
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.mystamp.R
import com.example.mystamp.ui.theme.MyStampTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import kotlin.math.absoluteValue

class TestActivity : ComponentActivity() {
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

    fun rememberRandomSampleImageUrl(): String {
        // 여기에서 임의의 이미지 URL을 생성하거나 가져옵니다.
        // 예를 들어, 이미지 URL 리스트가 있다면 그 중 하나를 무작위로 선택할 수 있습니다.

        val sampleImageUrls = listOf(
            "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg",
            "https://gongu.copyright.or.kr/gongu/wrt/cmmn/wrtFileImageView.do?wrtSn=13221193&filePath=L2Rpc2sxL25ld2RhdGEvMjAxOS8yMS9DTFMxMDAwNC8xMzIyMTE5M19XUlRfMjAxOTExMjFfMQ==&thumbAt=Y&thumbSe=b_tbumb&wrtTy=10004",
            "https://www.adobe.com/kr/creativecloud/photography/hub/guides/media_197108eb1be1977ee2a87738b4cd58efbf34f5ba7.jpeg?width=750&format=jpeg&optimize=medium",
            // 다른 이미지 URL을 추가하세요.
        )

        val randomIndex = (0 until sampleImageUrls.size).random()
        return sampleImageUrls[randomIndex]
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

    @OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
    @Composable
    fun HorizontalPagerWithOffsetTransition(modifier: Modifier = Modifier) {
        HorizontalPager(
            count = 3,
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
                    .aspectRatio(1f)
            ) {
                Column(modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Box {

                        Image(
                            painter = rememberImagePainter(
                                data = rememberRandomSampleImageUrl(),
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(),
                        )

                    }
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
}