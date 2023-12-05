package com.example.mystamp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.utils.AutoLogin
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class UserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    UserScreen()
                }
            }
        }
    }

    @Composable
    private fun UserScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    logoutAndDisabledAutoLogin()
                }
            ) {
                // 버튼에 로그아웃 텍스트 추가
                Text("로그아웃")
            }
        }

    }


    // 로그아웃 함수
    private fun logoutAndDisabledAutoLogin() {
        // 자동 로그인 해제 및 LoginActivity로 이동
        val autoLogin = AutoLogin(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            autoLogin.saveLoginStatus(false) // 자동 로그인 비활성화
        }
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    @Preview(showBackground = true)
    @Composable
    private fun PreviewUserScreen() {
        UserScreen()
    }
}