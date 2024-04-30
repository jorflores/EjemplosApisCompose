package com.example.ejemplosapis.service

import com.example.ejemplosapis.model.LoginRequest
import com.example.ejemplosapis.model.LoginResponse
import com.example.ejemplosapis.model.SignupRequest
import com.example.ejemplosapis.model.SignupResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface UserServiceApi {

    companion object {
        val instance: UserServiceApi = Retrofit.Builder().baseUrl("https://ejemplo-api-postgre.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserServiceApi::class.java)
    }

    @POST("users/login")
    suspend fun loginUser(@Body user: LoginRequest) : LoginResponse

    @POST("users/signup")
    suspend fun registerUser(@Body user: SignupRequest) : SignupResponse

}