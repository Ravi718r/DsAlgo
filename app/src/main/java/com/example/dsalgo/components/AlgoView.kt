package com.example.dsalgo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dsalgo.GlobalNavigation
import com.example.dsalgo.model.Algorithms
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun AlgoView(modifier: Modifier = Modifier) {
    val algoList = remember { mutableStateOf<List<Algorithms>>(emptyList()) }

    // 🔹 Fetch Firestore Data
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("algorithms")
            .get()
            .addOnSuccessListener { result ->
                algoList.value = result.documents.mapNotNull { it.toObject(Algorithms::class.java) }
            }
    }

    // 🔹 UI
    if (algoList.value.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(algoList.value) { algo ->
                AlgoIdCard(algo.id)
            }
        }
    }
}

@Composable
fun AlgoIdCard(id:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .clickable{
                GlobalNavigation.navController.navigate("algo-details/${id}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = id,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
