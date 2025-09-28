package com.example.dsalgo.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dsalgo.model.Algorithms
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AlgoDetailsPage(modifier: Modifier = Modifier, algoId: String) {
    val algoList = remember { mutableStateOf<List<Algorithms>>(emptyList()) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("algorithms")
            .whereEqualTo("id", algoId)
            .get()
            .addOnSuccessListener { result ->
                algoList.value = result.documents.mapNotNull { it.toObject(Algorithms::class.java) }
            }
    }

    val algo = algoList.value.firstOrNull()

    if (algo != null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Topic & Category
            Text(
                text = algo.topic,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = algo.category,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )

            // Description
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Description",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = algo.description, fontSize = 16.sp)
                }
            }

            // Complexity Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Time Complexity", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Best: ${algo.time_complexity.best}", fontSize = 16.sp)
                    Text("Average: ${algo.time_complexity.average}", fontSize = 16.sp)
                    Text("Worst: ${algo.time_complexity.worst}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Space Complexity: ${algo.space_complexity}", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Properties: ${listOfNotNull(algo.stable?.let { if (it) "Stable" else "Unstable" }, algo.in_place?.let { if (it) "In-place" else "Not In-place" }).joinToString(", ")}",
                        fontSize = 16.sp
                    )
                }
            }

            // Pseudo Code
            if (algo.pseudo_code.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pseudo Code", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            algo.pseudo_code,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Visualization
            if (algo.visualization.type.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD1C4E9)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Visualization", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Type: ${algo.visualization.type}", fontSize = 16.sp)
                        Text(algo.visualization.description, fontSize = 14.sp)
                        if (algo.visualization.example_steps.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Steps:", fontWeight = FontWeight.SemiBold)
                            algo.visualization.example_steps.forEach { step ->
                                Text("• $step", fontSize = 14.sp)
                            }
                        }
                    }
                }
            }

            // Code Snippet Section
            CodeSnippetCard(algo = algo)

            Spacer(modifier = Modifier.height(32.dp))
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun CodeSnippetCard(algo: Algorithms) {
    val languages = listOf("Python", "Java", "Kotlin", "C++", "JavaScript", "Rust")
    var selectedLang by remember { mutableStateOf("Python") }
    val scrollState = rememberScrollState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF37474F)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Language tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                languages.forEach { lang ->
                    Text(
                        text = lang,
                        color = if (lang == selectedLang) Color.White else Color.LightGray,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .clickable { selectedLang = lang }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .background(
                                color = if (lang == selectedLang) Color(0xFF546E7A) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Code content
            val codeText = when (selectedLang) {
                "Python" -> algo.code.python
                "Java" -> algo.code.java
                "Kotlin" -> algo.code.kotlin
                "C++" -> algo.code.cpp
                "JavaScript" -> algo.code.javascript
                "Rust" -> algo.code.rust
                else -> ""
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .background(Color(0xFF263238))
                    .padding(12.dp)
            ) {
                Text(
                    text = codeText.ifEmpty { "// No code available" },
                    color = Color(0xFF80CBC4),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
