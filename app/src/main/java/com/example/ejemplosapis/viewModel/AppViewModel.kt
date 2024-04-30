package com.example.ejemplosapis.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplosapis.model.LoginRequest
import com.example.ejemplosapis.model.LoginResponse
import com.example.ejemplosapis.model.SignupRequest
import com.example.ejemplosapis.model.SignupResponse
import com.example.ejemplosapis.service.UserServiceApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel (private val serviceApi: UserServiceApi) : ViewModel() {

     private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
     val loginResult = _loginResult.asStateFlow()

    private val _signupResult = MutableStateFlow<Result<SignupResponse>?>(null)
    val signupResult = _signupResult.asStateFlow()

    val isAuthenticated = MutableLiveData(false)

    fun authenticateUser(isValid: Boolean) {
        isAuthenticated.value = isValid
    }

    fun loginUser(user: LoginRequest) {

        viewModelScope.launch {

            try{
                val response = serviceApi.loginUser(user)
                _loginResult.value = Result.success(response)


            } catch(e: Exception){
                _loginResult.value = Result.failure(e)
            }

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

}