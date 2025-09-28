package com.example.dsalgo.page


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dsalgo.GlobalNavigation
import com.example.dsalgo.R

data class QuizSubject(
    val name: String,
    val imageRes: Int,
    val id: String
)

@Composable
fun QuizPage(
    modifier: Modifier = Modifier
) {
    val subjects = listOf(
        QuizSubject("Object-Oriented Programming (OOPs)", R.drawable.oops, "OOP"),
        QuizSubject("Computer Networks (CN)", R.drawable.cn, "CN"),
        QuizSubject("Operating System (OS)", R.drawable.os, "OS")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Choose a Subject",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1E293B)
        )

        subjects.forEach { subject ->
            SubjectCard(subject = subject) {
               GlobalNavigation.navController.navigate("quiz_details/${subject.id}")
            }
        }
    }
}

@Composable
fun SubjectCard(subject: QuizSubject, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = subject.imageRes),
                contentDescription = subject.name,
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = subject.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF334155),
                fontSize = 20.sp
            )
        }
    }
}
