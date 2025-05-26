package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import com.example.legalapor.models.MessageModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RiwayatChatViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private lateinit var messagesRef: CollectionReference

    private val _messages = MutableStateFlow<List<MessageModel>>(emptyList())
    val messages: StateFlow<List<MessageModel>> = _messages

    fun init(chatId: String) {
        if (::messagesRef.isInitialized) return

        messagesRef = firestore
            .collection("chats")
            .document(chatId)
            .collection("messages")

        listenForMessages()
    }

    private fun listenForMessages() {
        messagesRef
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener

                val messageList = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(MessageModel::class.java)
                }
                _messages.value = messageList
            }
    }

    fun sendMessage(text: String, receiverId: String) {
        val senderId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newMessage = MessageModel(
            messageId = messagesRef.document().id,
            senderId = senderId,
            receiverId = receiverId,
            message = text,
            timestamp = Timestamp.now()
        )

        messagesRef.document(newMessage.messageId).set(newMessage)
    }
}