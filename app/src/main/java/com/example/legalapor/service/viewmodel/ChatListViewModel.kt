package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legalapor.models.ChatModel
import com.example.legalapor.models.ChatPreviewModel
import com.example.legalapor.models.LawyerModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ChatListViewModel : ViewModel() {

    private val _chatList = MutableStateFlow<List<ChatModel>>(emptyList())
    private val _lawyerList = MutableStateFlow<List<LawyerModel>>(emptyList())

    val chatPreviews: StateFlow<List<ChatPreviewModel>> = combine(
        _chatList, _lawyerList
    ) { chats, lawyers ->
        chats.mapNotNull { chat ->
            val matchedLawyer = lawyers.find { it.id == chat.lawyerId }
            matchedLawyer?.let { lawyer ->
                ChatPreviewModel(chat = chat, lawyer = lawyer)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // buat debug
    init {
        _chatList.value = listOf(
            ChatModel(
                chatId = "chat1",
                userIds = listOf("user1", "lawyer1"),
                lastMessageText = "Halo, bagaimana kabarnya?",
                lastMessageTimestamp = Timestamp.now(),
                lawyerId = 1,
                qualification = "Hukum Keluarga"
            ),
            ChatModel(
                chatId = "chat2",
                userIds = listOf("user2", "lawyer2"),
                lastMessageText = "Terima kasih atas bantuan Anda.",
                lastMessageTimestamp = Timestamp.now(),
                lawyerId = 2,
                qualification = "Hukum Pidana"
            )
        )

        _lawyerList.value = listOf(
            LawyerModel(
                id = 1,
                name = "Reza Simanjuntak, S.H., M.H.",
                qualifications = "Hukum Keluarga",
                experience = "10 tahun",
                cases = "Perceraian, Sengketa Tanah",
                organization = "Lembaga Bantuan Hukum Jakarta",
                rating = 4.8f,
                reviewCount = 124,
                imageUrl = "https://picsum.photos/200"
            ),
            LawyerModel(
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
    }

}