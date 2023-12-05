package com.example.mystamp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystamp.R
import com.example.mystamp.ui.theme.AnchorGray
import com.example.mystamp.ui.theme.Cream
import androidx.compose.ui.unit.dp
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.ui.theme.SubYellow1
import com.example.mystamp.ui.theme.SubYellow2
import com.example.mystamp.ui.theme.SubYellow3
import com.example.mystamp.viewmodel.CouponViewModel
import com.example.mystamp.viewmodel.UserViewModel
import com.example.mystamp.utils.AutoLogin
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext

class UserActivity : ComponentActivity() {
    private val _viewModel = UserViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {

                    UserScreen()
                }
            }
        }
    }

    @Composable
    fun ProfileCard(
        modifier: Modifier = Modifier,
        name: String,
        phoneNumber: String,
        profileImageRes: Int
    ) {

        LaunchedEffect(_viewModel.fetchTrigger) {
            try {
                _viewModel.fetchUserData()
            } catch (e: Exception) {
                Log.e("NetworkError", "유저정보 가져오기 오류", e)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Cream)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp)
                        .clip(MaterialTheme.shapes.medium)
                ) {
                    // Circular profile image
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ){
                        Image(
                            painter = painterResource(id = profileImageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(4.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Name
                        Text(
                            text = _viewModel.userData.name,
                            style = MaterialTheme.typography.titleLarge,
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Phone number
                        Text(
                            text = _viewModel.userData.phoneNumber,
                            style = MaterialTheme.typography.bodyLarge,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row {
                            Text(
                                text = "스탬프보드 개수:",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = _viewModel.userData.stamps.size.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                        Row {
                            Text(
                                text = "쿠폰 개수:",
                                style = MaterialTheme.typography.bodyLarge,
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = _viewModel.userData.coupons.size.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }

                    }

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