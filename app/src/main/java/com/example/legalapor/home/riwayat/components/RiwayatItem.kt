package com.example.legalapor.home.riwayat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun RiwayatItem(
    lawyerName: String,
    lawyerTitle: String,
    lastMessage: String,
    date: String,
    profileImageUrl: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = profileImageUrl,
            contentDescription = null,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = lawyerName, style = MaterialTheme.typography.titleMedium)
            Text(text = lawyerTitle, style = MaterialTheme.typography.bodySmall)
            Text(text = lastMessage, style = MaterialTheme.typography.bodySmall, maxLines = 1)
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(text = date, style = MaterialTheme.typography.labelSmall)
    }
}

//private fun getInitials(name: String): String {
//    return name.split(" ")
//        .take(2)
//        .map { it.first().uppercaseChar() }
//        .joinToString("")
//}