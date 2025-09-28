package com.example.dsalgo.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dsalgo.GlobalNavigation

@Composable
fun QuizesDetailPage(
    modifier: Modifier = Modifier,
    subName: String,
    totalQuestions: Int = 90
) {
    var selectedQuestions by remember { mutableStateOf(10) }
    var selectedLevel by remember { mutableStateOf("Mix") }
    var showInfoDialog by remember { mutableStateOf(false) } // Dialog visibility state

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FC))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Subject Name
        Text(
            text = subName,
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1E293B)
        )

        // Total Questions Info
        Text(
            text = "Total Questions: $totalQuestions",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF475569)
        )

        // Select Number of Questions
        Text("Select Number of Questions", fontSize = 18.sp, color = Color(0xFF334155))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf(10, 20, 30).forEach { num ->
                FilterChip(
                    selected = selectedQuestions == num,
                    onClick = { selectedQuestions = num },
                    label = { Text("$num") }
                )
            }
        }

        // Select Difficulty Level
        Text("Select Difficulty", fontSize = 18.sp, color = Color(0xFF334155))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("Easy", "Medium", "Hard", "Mix").forEach { level ->
                FilterChip(
                    selected = selectedLevel == level,
                    onClick = { selectedLevel = level },
                    label = { Text(level) }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { showInfoDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF94A3B8))
            ) {
                Text("Info", color = Color.White)
            }

            Button(
                onClick = {
                    GlobalNavigation.navController.navigate("test/$subName/$selectedLevel/$selectedQuestions")
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text("Proceed Test", color = Color.White)
            }
        }
    }

    // Info Dialog
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = { Text(text = subName) },
            text = {
                Column {
                    Text("Scoring Rules:")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("+4 for each correct answer")
                    Text("-1 for each wrong answer")
                }
            },
            confirmButton = {
                TextButton(onClick = { showInfoDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
