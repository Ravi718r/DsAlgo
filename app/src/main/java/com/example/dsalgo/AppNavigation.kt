package com.example.dsalgo

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.dsalgo.page.AlgoDetailsPage
import com.example.dsalgo.page.QuizesDetailPage
import com.example.dsalgo.page.TestPage
import com.example.dsalgo.screen.AuthScreen
import com.example.dsalgo.screen.HomeScreen
import com.example.dsalgo.screen.LoginScreen
import com.example.dsalgo.screen.SignupScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    GlobalNavigation.navController = navController

    val isLoggedIn = Firebase.auth.currentUser!=null
    val firstPage = if(isLoggedIn) "home" else "auth"

    NavHost(
        navController = navController,
        startDestination = firstPage,
        modifier = modifier
    ) {
        composable("auth") { AuthScreen(navController) }
        composable("login") { LoginScreen(modifier ,navController) }
        composable("signup") { SignupScreen(modifier,navController) }
        composable("home") { HomeScreen(modifier,navController) }

        composable("algo-details/{algoId}"){
            val algoId = it.arguments?.getString("algoId")
            if(algoId != null){
                AlgoDetailsPage(modifier,algoId)
            }
        }

        composable("quiz_details/{subName}") {
            val subName = it.arguments?.getString("subName")
            if (subName != null) {
                QuizesDetailPage(modifier,subName)
            }
        }

        composable("test/{subName}/{level}/{questions}") { backStackEntry ->
            val subName = backStackEntry.arguments?.getString("subName") ?: ""
            val level = backStackEntry.arguments?.getString("level") ?: "Mix"
            val questions = backStackEntry.arguments?.getString("questions")?.toIntOrNull() ?: 10

            TestPage(
                modifier = modifier,
                subName = subName,
                level = level,
                questions = questions
            )
        }

    }
}

object GlobalNavigation{
    lateinit var navController : NavHostController
}
