package com.example.ejemplosapis.service

import com.example.ejemplosapis.model.GetAllUsersResponse
import com.example.ejemplosapis.model.LoginRequest
import com.example.ejemplosapis.model.LoginResponse
import com.example.ejemplosapis.model.SignupRequest
import com.example.ejemplosapis.model.SignupResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserServiceApi {

    companion object {

        private val okHttpClient: OkHttpClient
            get() = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()


        val instance: UserServiceApi = Retrofit.Builder().baseUrl("https://tc2007b-postgre.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(UserServiceApi::class.java)
    }

    @POST("users/loginToken")
    suspend fun loginUser(@Body user: LoginRequest) : LoginResponse

    @POST("users/signup")
    suspend fun registerUser(@Body user: SignupRequest) : SignupResponse

    @GET("users/allUsers")
    suspend fun getAllUsers(@Header("auth") token: String?)  : GetAllUsersResponse

}