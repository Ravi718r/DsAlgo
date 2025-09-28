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
fun SignupScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF00ACC1), Color(0xFF006064))
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
                    "Sign Up",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color(0xFF006064)
                )

                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; nameError = null },
                    enabled = !isLoading,
                    isError = nameError != null,
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    )
                )
                if (nameError != null) {
                    Text(nameError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; emailError = null },
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
                    Text(emailError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it; passwordError = null },
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
                            handleSignup(
                                name, email, password,
                                onError = { n, e, p -> nameError = n; emailError = e; passwordError = p },
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
                    Text(passwordError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                // Signup Button
                Button(
                    onClick = {
                        handleSignup(
                            name, email, password,
                            onError = { n, e, p -> nameError = n; emailError = e; passwordError = p },
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006064))
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Creating account", color = Color.White)
                    } else {
                        Text("Sign Up", color = Color.White)
                    }
                }

                TextButton(onClick = { navController.navigate("login") }) {
                    Text("Already have an account? Login", color = Color(0xFF006064))
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

// Validation + signup handler
private fun handleSignup(
    name: String,
    email: String,
    password: String,
    onError: (String?, String?, String?) -> Unit,
    onStart: () -> Unit,
    onFinish: () -> Unit,
    onSuccess: () -> Unit,
    onFail: (String?) -> Unit,
    authViewModel: AuthViewModel
) {
    if (name.isBlank()) {
        onError("Name required", null, null)
        return
    }
    if (!Pattern.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", email)) {
        onError(null, "Invalid email format", null)
        return
    }
    if (password.length < 6) {
        onError(null, null, "Password must be at least 6 characters")
        return
    }

    onStart()
    authViewModel.signup(email, password, name) { success, errorMessage ->
        onFinish()
        if (success) onSuccess() else onFail(errorMessage)
    }
}
