// File: RiwayatScreen.kt
package com.example.legalapor.home.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.legalapor.R
import com.example.legalapor.home.riwayat.components.RiwayatItem
import com.example.legalapor.home.riwayat.ui.theme.LegaLaporTheme
import com.example.legalapor.home.riwayat.ui.theme.BackgroundGray

data class RiwayatData(
    val id: Int,
    val lawyerName: String,
    val lawyerTitle: String,
    val lastMessage: String,
    val date: String,
    val profileImage: Int // Resource ID
)

@Composable
fun RiwayatScreen() {
    // Sample data - ganti dengan data real dari ViewModel/Repository
    val riwayatList = remember {
        listOf(
            RiwayatData(
                id = 1,
                lawyerName = "Reza Simanjuntak, S.H, M.H.",
                lawyerTitle = "Perkawinan dan Perceraian",
                lastMessage = "Laporan Ibu akan segera kami tanjuti...",
                date = "5 Sep",
                profileImage = R.drawable.lawyer1_reza // Ganti dengan resource gambar yang sesuai
            ),
            RiwayatData(
                id = 2,
                lawyerName = "Dr. Ahmad Santoso, S.H.",
                lawyerTitle = "Hukum Pidana",
                lastMessage = "Terima kasih atas penjelasan yang detail...",
                date = "3 Sep",
                profileImage = R.drawable.lawyer1_reza
            ),
            RiwayatData(
                id = 3,
                lawyerName = "Maria Susanti, S.H., M.Kn.",
                lawyerTitle = "Hukum Perdata",
                lastMessage = "Saya akan membantu menyelesaikan kasus ini...",
                date = "1 Sep",
                profileImage = R.drawable.lawyer1_reza
            )
        )
    }

    // Content - hanya list riwayat
    if (riwayatList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Belum ada riwayat chat",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(riwayatList) { riwayat ->
                RiwayatItem(
                    lawyerName = riwayat.lawyerName,
                    lawyerTitle = riwayat.lawyerTitle,
                    lastMessage = riwayat.lastMessage,
                    date = riwayat.date,
                    profileImage = riwayat.profileImage,
                    onClick = {
                        // Handle item click - navigate to chat detail
                        // Implementasi navigasi ke detail chat
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RiwayatScreenPreview() {
    LegaLaporTheme {
        RiwayatScreen()
    }
}