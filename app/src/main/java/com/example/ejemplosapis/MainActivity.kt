package com.example.ejemplosapis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ejemplosapis.screens.LoginScreen
import com.example.ejemplosapis.screens.RegisterScreen
import com.example.ejemplosapis.screens.ShellAuthenticatedScreen
import com.example.ejemplosapis.service.UserServiceApi
import com.example.ejemplosapis.ui.theme.EjemplosApisTheme
import com.example.ejemplosapis.viewModel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EjemplosApisTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val loginviewmodel = AppViewModel(UserServiceApi.instance,application)
                    val navController = rememberNavController()
                    MainScreen(navController, loginviewmodel)
                }
            }
        }
    }
}


@Composable
fun MainScreen(navController: NavHostController, appViewModel: AppViewModel) {
    //val isAuthenticated by appViewModel.isAuthenticated.observeAsState(false)
    val jwtToken = appViewModel.jwtToken.collectAsState()

    if (jwtToken.value.isNullOrBlank()) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") { LoginScreen(navController, appViewModel) }
            composable("register") { RegisterScreen(navController,appViewModel) }
            composable("home") { ShellAuthenticatedScreen(navController,appViewModel) }
        }
    } else
        ShellAuthenticatedScreen(navController = navController, viewModel = appViewModel)
}





