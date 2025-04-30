package com.example.bookingservice.ui.registration

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookingservice.viewmodel.AuthViewModel

@Composable
fun RegistrationScreen(
    viewModel: AuthViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onBackToLoginClick: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val registrationSuccess by viewModel.registrationSuccess.collectAsState()

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (registrationSuccess) {
        AlertDialog(
            onDismissRequest = {
                viewModel.resetRegistrationSuccess()
                onRegisterSuccess()
            },
            title = { Text("Registration Successful") },
            text = { Text("Your account has been created.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.resetRegistrationSuccess()
                    onRegisterSuccess()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Register",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.register(username, email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = username.isNotBlank() && email.isNotBlank() && password.isNotBlank()
            ) {
                Text("Register")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Login")
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}