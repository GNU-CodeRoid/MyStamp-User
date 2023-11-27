package com.example.mystamp.activity

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
import com.example.mystamp.ui.theme.MyStampTheme
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
}


interface ApiService {
    @POST("/user/signup")
    fun signUpUser(@Body userData: UserData): Call<Void>
}

data class UserData(
    val name: String,
    val phoneNumber: String
)

object RetrofitClient {
    private const val BASE_URL = "http://203.232.193.177:8080"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
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
        TextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("전화번호") }
        )

        TextField(
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
    val apiService = RetrofitClient.apiService
    val userData = UserData(name, phoneNumber)

    val call = apiService.signUpUser(userData)
    call.enqueue(object : Callback<Void> {
        override fun onResponse(call: Call<Void>, response: Response<Void>) {
            if (response.isSuccessful) {
                // Successful request handling
                Log.d("user/signup", "Success")
                activity.finish()
            } else {
                Log.d("user/signup", "Fail")
                // Handle request failure
                // You can use response.code(), response.message(), etc., to check the failure reason
            }
        }

        override fun onFailure(call: Call<Void>, t: Throwable) {
            // Handle network request failure
            Log.d("user/signup", "Fail(network) : $t")
        }
    })
}

//private fun register(activity: Activity){
//    activity.finish()
//}