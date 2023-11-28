package com.example.mystamp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdUnits
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mystamp.AppManager
import com.example.mystamp.ui.theme.MyStampTheme


class SplashActivity : ComponentActivity() {

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
                    init()

                }
            }
        }

    }


    @Preview()
    @Composable
    private fun ScreenPreview(){
        Screen()
    }

    @Composable
    private fun Screen(){
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .size(120.dp),
                    imageVector = Icons.Filled.AdUnits,
                    contentDescription = "앱 아이콘")
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = "마이 스탬프",
                    fontSize = 30.sp
                )
            }

        }
    }

    private fun init() {
        AppManager.init(applicationContext)
        if (AppManager.getStartInit() == true) {
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }, 1000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                AppManager.startInit()
                startActivity(Intent(this, MainActivity::class.java))
                finish()

            }, 1000)
        }
    }


}



