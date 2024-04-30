package com.example.ejemplosapis.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ejemplosapis.constants.Constants
import com.example.ejemplosapis.model.SignupRequest
import com.example.ejemplosapis.viewModel.AppViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavController, viewModel: AppViewModel) {
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var agreeToPrivacyPolicy by remember { mutableStateOf(false) }
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    val signupResult by viewModel.signupResult.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefono") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmar Contraseña") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column() {
                Checkbox(
                    checked = agreeToPrivacyPolicy,
                    onCheckedChange = { agreeToPrivacyPolicy = it }
                )
                Text("Acepto Aviso de Privacidad")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                            append("Ver Aviso de privacidad")
                        }
                    },
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showPrivacyPolicyDialog = true }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (agreeToPrivacyPolicy) {
                        viewModel.registerUser(SignupRequest(phone, password))
                    } else {
                      /*  snackbarHostState.showSnackbar(
                            message = "Debe aceptar el aviso de privacidad para continuar.",
                            duration = SnackbarDuration.Long
                        )*/
                    }
                },
                enabled = agreeToPrivacyPolicy
            ) {
                Text("Registrar")
            }

            signupResult?.let { result ->
                LaunchedEffect(result) {
                    if (result.isSuccess) {
                        snackbarHostState.showSnackbar(
                            message = "Registro exitoso. Redirigiendo a página principal.",
                            duration = SnackbarDuration.Short
                        )
                        viewModel.authenticateUser(true)
                        navController.navigate("home")
                    } else {
                        snackbarHostState.showSnackbar(
                            message = "Error: ${result.exceptionOrNull()?.message}",
                            duration = SnackbarDuration.Long,
                            actionLabel = "Reintentar"
                        )
                    }
                }
            }
        }

        if (showPrivacyPolicyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyPolicyDialog = false },
                title = { Text("Aviso de Privacidad") },
                text = {
                    LazyColumn(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)) {
                        item {
                            Text(
                                text = Constants.avisoDePrivacidad,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { showPrivacyPolicyDialog = false }) {
                        Text("Cerrar")
                    }
                }
            )
        }
    }
}
