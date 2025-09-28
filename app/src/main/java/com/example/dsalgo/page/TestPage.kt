package com.example.dsalgo.page

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dsalgo.model.QuizQuestion
import com.example.dsalgo.HighScoreManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestPage(
    modifier: Modifier = Modifier,
    subName: String,
    level: String,
    questions: Int
) {
    var quizQuestions by remember { mutableStateOf<List<QuizQuestion>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Quiz State
    var currentIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showExplanation by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var userAnswers by remember { mutableStateOf(mutableListOf<String?>()) }

    // Show finish dialog
    var showDialog by remember { mutableStateOf(false) }

    // Fetch Quiz
    LaunchedEffect(subName, level, questions) {
        try {
            val db = FirebaseFirestore.getInstance()
            val snapshot = db.collection("quiz")
                .document(subName)
                .get()
                .await()

            val quizModel = snapshot.toObject(com.example.dsalgo.model.QuizModel::class.java)
            val allQuestions = quizModel?.questions ?: emptyList()

            val filtered = if (level.lowercase() == "mix") allQuestions
            else allQuestions.filter { it.level.equals(level, ignoreCase = true) }

            quizQuestions = filtered.shuffled().take(questions)
            userAnswers = MutableList(quizQuestions.size) { null }

        } catch (e: Exception) {
            Log.e("TestPage", "Error fetching quiz: ", e)
        } finally {
            loading = false
        }
    }

    if (loading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        if (quizQuestions.isEmpty()) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No questions found for $subName - $level")
            }
        } else {
            val currentQuestion = quizQuestions[currentIndex]

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Question Counter + Score
                Text(
                    text = "Question ${currentIndex + 1} of ${quizQuestions.size} | Score: $score",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(12.dp))

                // Question
                Text(
                    text = currentQuestion.question,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(20.dp))

                // Options as Buttons
                currentQuestion.options.forEach { option ->
                    Button(
                        onClick = {
                            if (!showExplanation) {
                                selectedAnswer = option
                                showExplanation = true
                                userAnswers[currentIndex] = option

                                // +4 for correct, -1 for wrong
                                score += if (option == currentQuestion.correct_answer) 4 else -1
                                score = maxOf(score, 0) // prevent negative score
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = when {
                                selectedAnswer == null -> MaterialTheme.colorScheme.secondaryContainer
                                option == currentQuestion.correct_answer -> Color(0xFF4CAF50)
                                option == selectedAnswer && option != currentQuestion.correct_answer -> Color(0xFFF44336)
                                else -> MaterialTheme.colorScheme.secondaryContainer
                            },
                            contentColor = Color.Black
                        )
                    ) {
                        Text(option, fontSize = 16.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Explanation
                if (showExplanation) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Explanation:\n${currentQuestion.explanation}",
                            modifier = Modifier.padding(12.dp),
                            fontSize = 15.sp,
                            color = Color.DarkGray
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Next / Finish Button
                if (showExplanation) {
                    Button(
                        onClick = {
                            if (currentIndex < quizQuestions.size - 1) {
                                currentIndex++
                                selectedAnswer = null
                                showExplanation = false
                            } else {
                                // Update global high score
                                HighScoreManager.updateHighScore(subName, score)
                                showDialog = true
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            if (currentIndex < quizQuestions.size - 1) "Next" else "Finish",
                            color = Color.White
                        )
                    }
                }
            }

            // Finish Dialog
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    confirmButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("OK")
                        }
                    },
                    title = { Text("Quiz Finished") },
                    text = {
                        val correctCount = userAnswers.countIndexed { index, answer ->
                            answer == quizQuestions[index].correct_answer
                        }
                        val incorrectCount = userAnswers.size - correctCount
                        val maxScore = quizQuestions.size * 4
                        val percent = (score.toFloat() / maxScore * 100).toInt()

                        Column {
                            Text("Subject: $subName")
                            Text("Total Questions: ${quizQuestions.size}")
                            Text("Correct Answers: $correctCount")
                            Text("Incorrect Answers: $incorrectCount")
                            Text("Score: $score / $maxScore")
                            Text("Percentage: $percent%")
                            Text("Global High Score: ${HighScoreManager.getHighScore(subName)}")
                        }
                    }
                )
            }
        }
    }
}

// Helper for counting with index
inline fun <T> List<T>.countIndexed(predicate: (index: Int, T) -> Boolean): Int {
    var count = 0
    for ((i, element) in this.withIndex()) {
        if (predicate(i, element)) count++
    }
    return count
}
