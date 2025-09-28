package com.example.dsalgo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dsalgo.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun DsaHeaderView(
    name: String,
    title: String,
    onSearchClick: () -> Unit,
    onAccountClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Name + Title
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Right side: Search + Profile
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSearchClick() }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                painter = painterResource(id = R.drawable.outline_person_24),
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onAccountClick() }
            )
        }
    }
}
