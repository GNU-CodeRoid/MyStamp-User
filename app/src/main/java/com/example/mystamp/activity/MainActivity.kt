package com.example.mystamp.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TextButton
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystamp.ui.theme.MyStampTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    Screen()
                    val intent = Intent(this,TestActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Screen() {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 상단에 앱 이름 표시
        AppName(name = "마이 스탬프")

        // 나머지 화면 컨텐츠 추가
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)


        ) {

            //StackedCardsWithClick()

            val sliderList = listOf(
                "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg",
                "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg",
                "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg",
                "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg",
                "https://file.miricanvas.com/template_thumb/2019/05/24/3738-1558697059485/1558697059485/thumb.jpg"
            )
            Carousel(
                count = sliderList.size,
                contentWidth = 360.dp,
                contentHeight = 200.dp,
                content = { modifier, index ->
                    MyComposableContent(
                        item = sliderList[index],
                        modifier = modifier
                            .clickable {
                            }
                            .focusRequester(FocusRequester())
                    )
                }
            )
        }

    }
}


@Composable
fun AppName(name: String) {
    Text(
        text = name,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        color = Color.Black, // 원하는 색상으로 변경 가능
        textAlign = TextAlign.Center,
        fontSize = 36.sp
    )
}

@Composable
fun StackedCardsWithClick() {
    var cardIndex by remember { mutableStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        when(cardIndex){
            // Current card
            0 -> {
                CardWithClick(Color.Blue){
                    cardIndex +=1
                }
            }
            1 -> {
                CardWithClick(Color.Red){
                    cardIndex +=1
                }
            }
            2 -> {
                CardWithClick(Color.Yellow){
                    cardIndex +=1
                }
            }
            3 -> {
                CardWithClick(Color.Green){
                    cardIndex = 0
                }
            }
        }

    }
}

/*
* 스탬프 카드 디자인 함수
* */
@Composable
fun CardWithClick(
    color: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // 3분의 1 크기
            .background(color)
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // 내용 추가
    }
}


@Composable
@Preview
fun StackedCardsWithClickPreview() {
    //StackedCardsWithClick()

}