package com.example.legalapor.home.riwayat

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.legalapor.home.riwayat.components.RiwayatItem
import com.example.legalapor.home.riwayat.ui.theme.LegaLaporTheme
import com.example.legalapor.home.riwayat.ui.theme.BackgroundGray
import com.example.legalapor.models.ChatModel
import com.example.legalapor.models.ChatPreviewModel
import com.example.legalapor.models.LawyerModel
import com.example.legalapor.navigation.NavRoutes
import com.example.legalapor.service.viewmodel.ChatListViewModel
import com.example.legalapor.utils.formatDate
import com.google.firebase.Timestamp

@Composable
fun RiwayatScreen(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val navBackStackEntry = remember(currentBackStackEntry.value) {
        navController.getBackStackEntry(NavRoutes.Riwayat.route)
    }
    val viewModel: ChatListViewModel = viewModel(navBackStackEntry)

    val lifecycle = navBackStackEntry.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.fetchChatPreviews()
            }
        }
        lifecycle.addObserver(observer)

        onDispose {
            lifecycle.removeObserver(observer)
        }
    }

    val chatPreviews by viewModel.chatPreviews.collectAsState()




    if (chatPreviews.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada riwayat chat", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(chatPreviews) { preview ->
                RiwayatItem(
                    lawyerName = preview.lawyer.name,
                    lawyerTitle = preview.lawyer.qualifications,
                    lastMessage = preview.chat.lastMessageText,
                    date = formatDate(preview.chat.lastMessageTimestamp),
                    profileImageUrl = preview.lawyer.imageUrl,
                    onClick = {
                        navController.navigate(
                            "chat/${preview.chat.chatId}/${preview.lawyer.id}/${Uri.encode(preview.lawyer.name)}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun RiwayatScreenPreview() {
    val dummyChatPreviews = listOf(
        ChatPreviewModel(
            chat = ChatModel(
                chatId = "chat1",
                userIds = listOf("user1", "lawyer1"),
                lastMessageText = "Halo, apa kabar?",
                lastMessageTimestamp = Timestamp.now(),
                lawyerId = 1,
                qualification = "Hukum Perdata"
            ),
            lawyer = LawyerModel(
                id = 1,
                name = "Reza Simanjuntak, S.H., M.H.",
                qualifications = "Hukum Perdata",
                experience = "10 tahun",
                cases = "Perceraian, Sengketa Tanah",
                organization = "Lembaga Bantuan Hukum Jakarta",
                rating = 4.8f,
                reviewCount = 124,
                imageUrl = "https://picsum.photos/200"
            )
        ),
        ChatPreviewModel(
            chat = ChatModel(
                chatId = "chat2",
                userIds = listOf("user2", "lawyer2"),
                lastMessageText = "Terima kasih atas bantuan Anda.",
                lastMessageTimestamp = com.google.firebase.Timestamp.now(),
                lawyerId = 2,
                qualification = "Hukum Pidana"
            ),
            lawyer = LawyerModel(
                id = 2,
                name = "Maria Susanti, S.H., M.Kn.",
                qualifications = "Hukum Pidana",
                experience = "8 tahun",
                cases = "Kasus Kriminal",
                organization = "Kantor Pengacara Jakarta",
                rating = 4.6f,
                reviewCount = 80,
                imageUrl = "https://picsum.photos/201"
            )
        )
    )

    LegaLaporTheme {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundGray),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dummyChatPreviews) { preview ->
                RiwayatItem(
                    lawyerName = preview.lawyer.name,
                    lawyerTitle = preview.lawyer.qualifications,
                    lastMessage = preview.chat.lastMessageText,
                    date = formatDate(preview.chat.lastMessageTimestamp),
                    profileImageUrl = preview.lawyer.imageUrl,
                    onClick = {}
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewRiwayatScreen() {
    LegaLaporTheme {
        RiwayatScreenPreview()
    }
}