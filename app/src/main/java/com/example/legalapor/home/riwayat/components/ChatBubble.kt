package com.example.legalapor.home.riwayat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatBubble(
    message: String,
    time: String,
    isFromUser: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = if (isFromUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            horizontalAlignment = if (isFromUser) Alignment.End else Alignment.Start,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = if (isFromUser) Color(0xFF4A90E2) else Color(0xFFE8E8E8),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isFromUser) 16.dp else 4.dp,
                            bottomEnd = if (isFromUser) 4.dp else 16.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = message,
                    color = if (isFromUser) Color.White else Color.Black,
                    fontSize = 14.sp
                )
            }

            Text(
                text = time,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    top = 4.dp,
                    start = if (isFromUser) 0.dp else 8.dp,
                    end = if (isFromUser) 8.dp else 0.dp
                )
            )
        }

        if (isFromUser) {
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}