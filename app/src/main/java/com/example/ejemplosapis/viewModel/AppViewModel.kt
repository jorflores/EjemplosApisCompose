package com.example.ejemplosapis.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplosapis.model.GetAllUsersResponse
import com.example.ejemplosapis.model.LoginRequest
import com.example.ejemplosapis.model.LoginResponse
import com.example.ejemplosapis.model.SignupRequest
import com.example.ejemplosapis.model.SignupResponse
import com.example.ejemplosapis.service.UserServiceApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel (private val serviceApi: UserServiceApi, application: Application) : AndroidViewModel(application) {


    private val prefs = application.getSharedPreferences("Settings", Context.MODE_PRIVATE)

  /*  private val _isInitialized = MutableStateFlow(false)
    val isInitialized = _isInitialized.asStateFlow()*/

     private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
     val loginResult = _loginResult.asStateFlow()

    private val _signupResult = MutableStateFlow<Result<SignupResponse>?>(null)
    val signupResult = _signupResult.asStateFlow()

    private val _getAllUsersResult = MutableStateFlow<Result<GetAllUsersResponse>?>(null)
    val getAllUsersResult = _getAllUsersResult.asStateFlow()

    private val _jwtToken = MutableStateFlow(prefs.getString("jwtToken",""))
    val jwtToken = _jwtToken.asStateFlow()



    fun loginUser(user: LoginRequest) {
        _loginResult.value = null

        viewModelScope.launch {

            try{
                val response = serviceApi.loginUser(user)
                _loginResult.value = Result.success(response)
                setJwtToken(response.token)


            } catch(e: Exception){
                _loginResult.value = Result.failure(e)
            }

        }

    }

    fun logoutUser() {
        setJwtToken(null)
        _loginResult.value = null
    }


    private fun setJwtToken(token: String?) {

        viewModelScope.launch {
            prefs.edit().putString("jwtToken",token).apply()
            _jwtToken.value = token
        }
    }

    fun registerUser(user: SignupRequest) {

        viewModelScope.launch {

            try{
                val response = serviceApi.registerUser(user)
                _signupResult.value  =Result.success(response)

            } catch(e: Exception){
                _signupResult.value  =Result.failure(e)
            }

        }

    }


    fun getAllUsers(){

        viewModelScope.launch {
            try{
                val response = serviceApi.getAllUsers(jwtToken.value)
                _getAllUsersResult.value  =Result.success(response)

            } catch(e: Exception){
                _getAllUsersResult.value  =Result.failure(e)
            }

        }
    }

}