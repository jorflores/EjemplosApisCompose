package com.example.ejemplosapis.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.ejemplosapis.model.User
import com.example.ejemplosapis.viewModel.AppViewModel

@Composable
fun HomeScreen(viewModel: AppViewModel) {

    val jwtToken = viewModel.jwtToken.collectAsState()

    val getAllUsersResult by viewModel.getAllUsersResult.collectAsState()

    Column {
        Text("Welcome to Home Screen")
        Text(text = "Token: ${jwtToken.value}")
        Button(onClick = {

            viewModel.getAllUsers()

        }) {
            Text(text = "GetAllUsers")
        }


        getAllUsersResult?.let { result ->

            if (result.isSuccess) {
                val users = result.getOrNull()?.user ?: emptyList<User>()

                LazyColumn() {
                    items(items = users) {
                        Text("Name: ${it.phone}", style = MaterialTheme.typography.headlineMedium)
                        Text("Email: ${it.role}", style = MaterialTheme.typography.headlineMedium)
                    }
                }

            }

        }

    }
}