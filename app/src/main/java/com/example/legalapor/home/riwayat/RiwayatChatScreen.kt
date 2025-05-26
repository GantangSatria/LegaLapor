package com.example.legalapor.home.riwayat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.legalapor.home.riwayat.components.ChatBubble
import com.example.legalapor.home.riwayat.components.RiwayatChatTopAppBar
import com.example.legalapor.models.LawyerModel
import com.example.legalapor.service.viewmodel.RiwayatChatViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.legalapor.home.riwayat.components.MessageInputRow
import com.google.firebase.auth.FirebaseAuth

//class RiwayatChatScreen {
//}

@Composable
fun RiwayatChatScreen(
    navController: NavHostController,
    lawyer: LawyerModel,
    chatId: String,
) {
    val chatViewModel: RiwayatChatViewModel = viewModel()
    val messages by chatViewModel.messages.collectAsState()
    val listState = rememberLazyListState()
    var message by remember { mutableStateOf("") }

    LaunchedEffect(chatId) {
        chatViewModel.init(chatId)
    }

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            RiwayatChatTopAppBar(
                lawyer = lawyer,
                onNavigateBack = { navController.popBackStack() }
            )
        },
        bottomBar = {
            MessageInputRow(
                message = message,
                onMessageChange = { message = it },
                onSendMessage = {
                    chatViewModel.sendMessage(message, lawyer.id.toString())
                    message = ""
                }
            )
        }


    ) {innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp)
        ) {
            items(messages) { message ->
                ChatBubble(message = message.message,
                    time = message.timestamp,
                    isFromUser = message.senderId == FirebaseAuth.getInstance().currentUser?.uid)
            }
        }

    }
}

