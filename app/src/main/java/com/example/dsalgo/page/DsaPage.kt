package com.example.dsalgo.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.dsalgo.GlobalNavigation
import com.example.dsalgo.JsonToKotlin.QuizUploadScreen
import com.example.dsalgo.JsonToKotlin.UploadScreen
import com.example.dsalgo.components.AlgoView
import com.example.dsalgo.components.DsaHeaderView


@Composable
fun DsaPage(
    modifier: Modifier = Modifier
) {
    Column() {
        // 🔹 Custom Header
        DsaHeaderView(
            name = "Ravi",
            title = "Android Developer",
            onSearchClick = { /*TODO*/ },
            onAccountClick = { GlobalNavigation.navController.navigate("profile") }
        )

        Spacer(modifier = Modifier.height(8.dp)) // smaller gap
//        UploadScreen()
//        QuizUploadScreen()
        Divider(thickness = 1.dp)

        // 🔹 Algorithm list (no extra bottom padding)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {
            AlgoView()
        }
    }
}