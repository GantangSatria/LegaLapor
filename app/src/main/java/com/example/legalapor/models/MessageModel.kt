package com.example.legalapor.models

import com.google.firebase.Timestamp

data class MessageModel(
    val messageId: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timestamp: Timestamp,
)
