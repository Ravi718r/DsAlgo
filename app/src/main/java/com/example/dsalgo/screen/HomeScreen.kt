package com.example.dsalgo.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dsalgo.chatbot.ChatViewModel
import com.example.dsalgo.chatbot.data.model.Message
import com.example.dsalgo.model.ChatMessage
import com.example.dsalgo.page.ChatScreen
import com.example.dsalgo.page.DsaPage
import com.example.dsalgo.page.FavouritePage
import com.example.dsalgo.page.ProfilePage
import com.example.dsalgo.page.QuizPage

@Composable
fun HomeScreen(modifier: Modifier = Modifier , navController: NavController){

    val navItemList = listOf(
        NavItem("DSA",Icons.Default.Star),
        NavItem("Quiz",Icons.Default.Face),
        NavItem("AI",Icons.Default.Create),
        NavItem("Favourite",Icons.Default.Favorite),
        NavItem("Info",Icons.Default.Person),
    )

    var selectedIndex by rememberSaveable {
        mutableStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed{index, navItem ->
                    NavigationBarItem(
                        selected  = index == selectedIndex ,
                        onClick =  {
                            selectedIndex = index
                        },
                        icon = {
                            Icon(imageVector = navItem.icon, contentDescription = navItem.label)
                        },
                        label = {
                            Text(text = navItem.label)
                        }
                    )
                }
            }
        }
    ) {
        ContentScreen(
            modifier = modifier.padding(it),
            selectedIndex,
            navController
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier, selectedIndex : Int , navController: NavController){
    when(selectedIndex){
        0-> DsaPage(modifier  )
        1-> QuizPage(modifier)
        2-> ChatScreen( modifier , hiltViewModel<ChatViewModel>())
        3-> FavouritePage(modifier)
        4-> ProfilePage(modifier)
    }
}

data class NavItem(
    val label : String,
    val icon : ImageVector,
)