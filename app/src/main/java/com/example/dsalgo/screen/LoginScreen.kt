package com.example.dsalgo.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dsalgo.AppUtil
import com.example.dsalgo.authviewmodel.AuthViewModel
import java.util.regex.Pattern

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Login",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF4A00E0)
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        emailError = null
                    },
                    enabled = !isLoading,
                    isError = emailError != null,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                if (emailError != null) {
                    Text(
                        text = emailError!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        passwordError = null
                    },
                    enabled = !isLoading,
                    isError = passwordError != null,
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        Text(
                            text = if (passwordVisible) "Hide" else "Show",
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus()
                            handleLogin(
                                email, password,
                                onError = { e, p -> emailError = e; passwordError = p },
                                onStart = { isLoading = true },
                                onFinish = { isLoading = false },
                                onSuccess = {
                                    navController.navigate("home") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                },
                                onFail = { msg ->
                                    AppUtil.showToast(context, msg ?: "Something went wrong")
                                },
                                authViewModel
                            )
                        }
                    )
                )
                if (passwordError != null) {
                    Text(
                        text = passwordError!!,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Login Button
                Button(
                    onClick = {
                        handleLogin(
                            email, password,
                            onError = { e, p -> emailError = e; passwordError = p },
                            onStart = { isLoading = true },
                            onFinish = { isLoading = false },
                            onSuccess = {
                                navController.navigate("home") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            },
                            onFail = { msg ->
                                AppUtil.showToast(context, msg ?: "Something went wrong")
                            },
                            authViewModel
                        )
                    },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A00E0))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logging In", color = Color.White)
                    } else {
                        Text("Login", color = Color.White)
                    }
                }

                TextButton(onClick = { navController.navigate("signup") }) {
                    Text("Don't have an account? Sign Up", color = Color(0xFF4A00E0))
                }
            }
        }

        // Loading Overlay
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

// Helper Function for login flow
private fun handleLogin(
    email: String,
    password: String,
    onError: (String?, String?) -> Unit,
    onStart: () -> Unit,
    onFinish: () -> Unit,
    onSuccess: () -> Unit,
    onFail: (String?) -> Unit,
    authViewModel: AuthViewModel
) {
    if (!Pattern.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", email)) {
        onError("Invalid email format", null)
        return
    }
    if (password.isEmpty()) {
        onError(null, "Password cannot be empty")
        return
    }

    onStart()
    authViewModel.login(email, password) { success, errorMessage ->
        onFinish()
        if (success) onSuccess() else onFail(errorMessage)
    }
}
