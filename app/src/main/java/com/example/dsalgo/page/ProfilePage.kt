package com.example.dsalgo.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dsalgo.AppUtil
import com.example.dsalgo.GlobalNavigation
import com.example.dsalgo.HighScoreManager
import com.example.dsalgo.R
import com.example.dsalgo.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun ProfilePage(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var userModel by remember { mutableStateOf(UserModel()) }
    var addressInput by remember { mutableStateOf("") }

    // Fetch user data from Firestore
    LaunchedEffect(Unit) {
        try {
            val snapshot = FirebaseFirestore.getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                .get()
                .await()

            snapshot.toObject(UserModel::class.java)?.let {
                userModel = it
                addressInput = it.address
            }
        } catch (e: Exception) {
            AppUtil.showToast(context, "Failed to fetch user data")
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE3F2FD), Color(0xFFBBDEFB))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "Your Profile",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF0D47A1)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Profile Image
            Image(
                painter = painterResource(id = R.drawable.cat),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(Color.White)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // User Name
            Text(
                text = userModel.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0D47A1),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // User Info Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // Address Field
                    Text(
                        text = "Address",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0D47A1)
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = addressInput,
                        onValueChange = { addressInput = it },
                        placeholder = { Text("Enter your address") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions {
                            if (addressInput.isNotEmpty()) {
                                FirebaseFirestore.getInstance()
                                    .collection("users")
                                    .document(FirebaseAuth.getInstance().currentUser?.uid ?: "")
                                    .update("address", addressInput)
                                    .addOnSuccessListener {
                                        AppUtil.showToast(context, "Address Updated")
                                    }
                                    .addOnFailureListener {
                                        AppUtil.showToast(context, "Failed to update address")
                                    }
                            } else {
                                AppUtil.showToast(context, "Address cannot be empty")
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Email
                    ProfileDetails(label = "Email", value = userModel.email)

                    Spacer(modifier = Modifier.height(16.dp))

                    // High Scores
                    ProfileDetails(
                        label = "High Score - OOP",
                        value = HighScoreManager.getHighScore("OOP").toString()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileDetails(
                        label = "High Score - CN",
                        value = HighScoreManager.getHighScore("CN").toString()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProfileDetails(
                        label = "High Score - OS",
                        value = HighScoreManager.getHighScore("OS").toString()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Out Button
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    GlobalNavigation.navController.apply {
                        popBackStack()
                        navigate("auth")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D47A1)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Sign Out",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ProfileDetails(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF0D47A1)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, fontSize = 16.sp, color = Color.DarkGray)
    }
}
