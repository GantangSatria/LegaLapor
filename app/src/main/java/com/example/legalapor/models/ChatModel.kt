package com.example.legalapor.models

import com.google.firebase.Timestamp

data class ChatModel(
    val chatId: String = "",
    val userIds: List<String> = listOf(),
    val lastMessageText: String = "Start chatting…",
    val lastMessageTimestamp: Timestamp = Timestamp.now(),
    val lawyerId: Int = 0,
    val qualification: String = ""
)
