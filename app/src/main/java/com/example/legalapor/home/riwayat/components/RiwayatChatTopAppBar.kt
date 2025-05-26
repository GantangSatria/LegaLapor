package com.example.legalapor.home.riwayat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.legalapor.models.LawyerModel


@Composable
fun RiwayatChatTopAppBar(
    lawyer: LawyerModel,
    onNavigateBack: () -> Unit
) {
    println("RiwayatChatTopAppBar imageUrl = ${lawyer.imageUrl}")
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 150.dp)
            .background(
                color = Color(0xFF31469F),
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
            .statusBarsPadding()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.White
                )
            }
            AsyncImage(
                model = lawyer.imageUrl,
                contentDescription = "Profile picture of ${lawyer.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(53.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = lawyer.name,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, name = "Riwayat Chat Top App Bar Preview")
@Composable
fun RiwayatChatTopAppBarStandalonePreview() {
        RiwayatChatTopAppBar(
            lawyer = LawyerModel(
                id = 1,
                name = "Reza Simanjuntak, S.H., M.H.",
                imageUrl = "https://placehold.co/40x40/D1C4E9/7E57C2?text=RS"
            ),
            onNavigateBack = {}
        )
}