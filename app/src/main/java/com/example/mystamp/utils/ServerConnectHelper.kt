package com.example.mystamp.utils


import android.util.Log
import com.example.mystamp.dto.RequestLoginData
import com.example.mystamp.dto.ShopData
import com.example.mystamp.dto.StampBoard
import com.google.gson.GsonBuilder
import com.example.mystamp.dto.RequestAddStampData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * 통신을 위한 Helper 클래스
 */
class ServerConnectHelper {

    private val apiService: ApiService
    private var requestStampBoard: RequestStampBoard? = null
    var requestStampBoards: RequestStampBoards? = null
    var requestAddStamp: RequestAddStamp? = null
    var requestDeleteStamp: RequestDeleteStamp? = null
    var requestLogin: RequestLogin? = null


    init {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://203.232.193.177:8080/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)  // 여기에 추가
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    /**
     * 서버로부터 응답된 결과에 따라 인터페이스 실행
     */

    fun getStampBoard(phoneNumber: String, businessNumber: String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = apiService.getStampBoard(phoneNumber, businessNumber)
                val response = call.execute()

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        requestStampBoard!!.onSuccess(response.body()!!)

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        requestStampBoard!!.onFailure()
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    requestStampBoard?.onFailure()
                }
            }

        }
    }

    fun getStampBoards(phoneNumber: String){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = apiService.getStampBoards(phoneNumber)
                val response = call.execute()

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        requestStampBoards?.onSuccess(response.body()!!)

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        requestStampBoards?.onFailure()
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    requestStampBoards?.onFailure()
                }
            }

        }
    }

    fun addStamp(requestAddStampData: RequestAddStampData){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = apiService.addStamp(requestAddStampData)
                val response = call.execute()



                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        requestAddStamp?.onSuccess(response.body()!!)

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        requestAddStamp?.onFailure()
                        Log.d("test", response.errorBody()?.string().toString())
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    requestAddStamp?.onFailure()
                    Log.d("test",e.toString())
                }
            }

        }
    }

    fun deleteStamp(phoneNumber: String, businessNumber: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val call = apiService.deleteStamp(phoneNumber, businessNumber)
                val response = call.execute()

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {

                        requestDeleteStamp?.onSuccess(response.body()!!)

                    }
                } else {
                    withContext(Dispatchers.Main) {
                        requestDeleteStamp?.onFailure()
                    }
                }
            }catch (e: Exception){
                withContext(Dispatchers.Main) {
                    requestDeleteStamp?.onFailure()
                }
            }

        }
    }

    fun postLogin(phoneNumber: RequestLoginData) {
        CoroutineScope(Dispatchers.IO).launch {
            try {


                val call = apiService.postLogin(phoneNumber)
                val response = call.execute()
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Log.d("LoginResponse", "success")
                        requestLogin!!.onSuccess(response.body()!!)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.d("LoginResponse", "else")
                        requestLogin!!.onFailure()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginResponse", "$e")
                    requestLogin?.onFailure()
                }
            }
        }
    }



    /**
     * retrofit api 인터페이스
     */
    interface ApiService {
        @GET("stamp")
        fun getStampBoard(
            @Query("phoneNumber") phoneNumber : String,
            @Query("businessNumber") businessNumber : String,
        ): Call<ShopData>

        @GET("stamp/list")
        fun getStampBoards(
            @Query("phoneNumber") phoneNumber : String,
        ): Call<List<ShopData>>

        @POST("stamp")
        fun addStamp(
            @Body requestAddStampData: RequestAddStampData
        ): Call<String>

        @DELETE("stamp")
        fun deleteStamp(
            @Query("phoneNumber") phoneNumber: String,
            @Query("businessNumber") businessNumber : String,
        ): Call<String>

        @POST("user/login")
        fun postLogin(
            @Body requestLoginData: RequestLoginData
        ): Call<String>



    }

    /**
     * 서버 응답 인터페이스
     */
    interface RequestStampBoard {
        fun onSuccess(shopData: ShopData)
        fun onFailure()

    }

    interface RequestStampBoards {
        fun onSuccess(data: List<ShopData>)
        fun onFailure()

    }

    interface RequestAddStamp {
        fun onSuccess(message: String)
        fun onFailure()

    }

    interface RequestDeleteStamp {
        fun onSuccess(message: String)
        fun onFailure()

    }

    interface RequestLogin {
        fun onSuccess(data: String)
        fun onFailure()

    }






}
