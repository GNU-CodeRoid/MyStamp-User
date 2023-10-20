package com.example.mystamp.activity

import android.content.Intent
import android.os.Bundle
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


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                    val intent = Intent(this,RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun Screen() {
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
            StackedCardsWithClick()
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
    StackedCardsWithClick()
}