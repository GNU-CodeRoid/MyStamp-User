package com.example.mystamp.utils


import com.example.mystamp.dto.Stamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * 통신을 위한 Helper 클래스
 */
class ServerConnectHelper {

    private val apiService: ApiService
    var request: RequestServer? = null




    init {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl("http://203.232.193.177:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)  // 여기에 추가
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    /**
     * 서버로부터 응답된 결과에 따라 인터페이스 실행
     */








    /**
     * retrofit api 인터페이스
     */
    interface ApiService {
        @GET("stamp")
        fun getPost(): Call<Stamp>


    }

    /**
     * 서버 응답 인터페이스
     */
    interface RequestServer {
        fun onSuccess(testPost: Stamp)
        fun onFailure()

    }






}
