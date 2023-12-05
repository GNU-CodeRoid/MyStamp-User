package com.example.mystamp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystamp.AppManager
import com.example.mystamp.dto.RequestLoginData
import com.example.mystamp.dto.ShopData
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.utils.AutoLogin
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen() {
    var phoneNumber by remember { mutableStateOf("") }

    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("마이 스탬프")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("전화번호") },
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { login(activity, phoneNumber) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }
        Button(
            onClick = { toRegister(activity) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("회원가입")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    MyStampTheme {
        LoginScreen()
    }
}

private fun login(activity: Activity, phoneNumber: String) {
    CoroutineScope(Dispatchers.Main).launch {
        val serverConnectHelper = ServerConnectHelper()
        serverConnectHelper.requestLogin = object : ServerConnectHelper.RequestLogin {
            override fun onSuccess(data: String) {
                Log.d("LoginResponse", "Received data: $data")
                if (data.contains("login success")) {
                    CoroutineScope(Dispatchers.IO).launch {
                        // 로그인 성공 시 DataStore에 저장
                        val autoLogin = AutoLogin(activity)
                        autoLogin.savePhoneNumber(phoneNumber) // 전화번호 저장
                        autoLogin.saveLoginStatus(true) // 로그인 상태를 true로 저장
                    }
                    AppManager.setUid(phoneNumber)
                    Toast.makeText(activity, "로그인에 성공하였습니다", Toast.LENGTH_SHORT).show()
                    activity.finish()
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                } else {
                    Toast.makeText(activity, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    Log.e("LoginResponse", "Failed to login")
                }
            }
            override fun onFailure() {
                Toast.makeText(activity, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show()
                Log.e("LoginResponse", "Failed to receive data")
            }
        }
        val requestLoginData = RequestLoginData(phoneNumber)
        serverConnectHelper.postLogin(requestLoginData)
    }
}


private fun toRegister(activity: Activity){
    val intent = Intent(activity, RegisterActivity::class.java)
    activity.startActivity(intent)
}