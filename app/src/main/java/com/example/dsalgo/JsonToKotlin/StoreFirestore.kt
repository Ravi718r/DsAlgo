package com.example.dsalgo.JsonToKotlin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.dsalgo.AppUtil
import com.example.dsalgo.R;
import com.example.dsalgo.model.Algorithms
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Composable
fun StoreFirestore(){
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        val jsonString = AppUtil.readJsonFromraw(context, R.raw.algorithms)

        val gson = Gson()
        val listType = object : TypeToken<List<Algorithms>>() {}.type
        val algorithms: List<Algorithms> = gson.fromJson(jsonString, listType)

        val db = FirebaseFirestore.getInstance()

        algorithms.forEach { algo ->
            db.collection("algorithms")
                .document(algo.id) // unique id from JSON
                .set(algo)
                .addOnSuccessListener {
                    Log.d("Firestore", "Success: ${algo.id}")
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Failed: ${algo.id}", e)
                }
        }
    }
}

@Composable
fun UploadScreen(){
    var uploadtriggered = remember { mutableStateOf(false) }

    Column {
        Button(onClick = {
            uploadtriggered.value = true
        }) {
            if(uploadtriggered.value){
                StoreFirestore()
                Text("Uploading Data" , color = androidx.compose.ui.graphics.Color.White)
            }
        }
    }
}