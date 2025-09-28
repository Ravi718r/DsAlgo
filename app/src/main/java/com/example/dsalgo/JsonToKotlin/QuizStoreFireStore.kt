package com.example.dsalgo.JsonToKotlin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.dsalgo.AppUtil
import com.example.dsalgo.R
import com.example.dsalgo.model.QuizModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

@Composable
fun StoreQuizFirestore() {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // List of quiz files + doc ids
        val quizFiles = listOf(
            "oops.json" to "OOP",
            "cn.json" to "CN",
            "os.json" to "OS"
        )

        val db = FirebaseFirestore.getInstance()

        quizFiles.forEach { (fileName, docId) ->
            val jsonString = AppUtil.readJsonFromraw(
                context,
                context.resources.getIdentifier(
                    fileName.removeSuffix(".json"),
                    "raw",
                    context.packageName
                )
            )

            val gson = Gson()
            val quizModel: QuizModel = gson.fromJson(jsonString, QuizModel::class.java)

            db.collection("quiz")
                .document(docId)
                .set(quizModel)
                .addOnSuccessListener {
                    Log.d("Firestore", "Uploaded quiz: $docId")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed quiz: $docId", e)
                }
        }
    }
}

@Composable
fun QuizUploadScreen() {
    val uploadTriggered = remember { mutableStateOf(false) }

    Column {
        Button(onClick = { uploadTriggered.value = true }) {
            Text("Upload Quiz Data", color = Color.White)
        }

        if (uploadTriggered.value) {
            StoreQuizFirestore()
        }
    }
}
