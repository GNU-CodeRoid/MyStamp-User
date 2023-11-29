package com.example.mystamp.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mystamp.dto.RequestLoginData
import com.example.mystamp.dto.RequestRegisterData
import com.example.mystamp.ui.theme.MyStampTheme
import com.example.mystamp.utils.ServerConnectHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyStampTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegistrationScreen()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun RegistrationScreen() {
        var userName by remember { mutableStateOf("") }
        var phoneNumber by remember { mutableStateOf("") }


        val activity = LocalContext.current as Activity

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("회원 등록")
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("전화번호") }
            )

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = userName,
                onValueChange = { userName = it },
                label = { Text("이름") },
                //visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { register(activity, userName, phoneNumber) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("가입하기")
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun RegistrationScreenPreview() {
        MyStampTheme {
            RegistrationScreen()
        }
    }

    private fun register(activity: Activity, name: String, phoneNumber: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val serverConnectHelper = ServerConnectHelper()
            serverConnectHelper.requestRegister = object : ServerConnectHelper.RequestRegister {
                override fun onSuccess(data: String) {
                    Log.d("RegisterResponse", "Received data: $data")
                    if (data.contains("success")) {
                        Toast.makeText(activity, "가입에 성공하였습니다", Toast.LENGTH_SHORT).show()
                        activity.finish()
                        val intent = Intent(activity, LoginActivity::class.java)
                        activity.startActivity(intent)
                    } else if (data.contains("This phone number already exists")) {
                        Toast.makeText(activity, "이미 존재하는 번호입니다", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterResponse", "This phone number already exists")
                    } else {
                        Toast.makeText(activity, "가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
                        Log.e("RegisterResponse", "Failed to signup")
                    }

                }

                override fun onFailure() {
                    Log.e("RegisterResponse", "Failed to receive data")
                    Toast.makeText(activity, "가입에 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }
            val requestRegisterData = RequestRegisterData(name, phoneNumber)
            serverConnectHelper.postRegister(requestRegisterData)
        }
    }

}
