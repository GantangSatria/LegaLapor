package com.example.legalapor.service.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legalapor.models.ChatModel
import com.example.legalapor.models.ChatPreviewModel
import com.example.legalapor.models.LawyerModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatListViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _chatPreviews = MutableStateFlow<List<ChatPreviewModel>>(emptyList())
    val chatPreviews: StateFlow<List<ChatPreviewModel>> = _chatPreviews

    companion object {
        private const val TAG = "ChatListViewModel"
    }

    init {
        fetchChatPreviews()
    }

    fun fetchChatPreviews() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d(TAG, "fetchChatPreviews called")
        Log.d(TAG, "Current user ID: $userId")

        if (userId == null) {
            Log.e(TAG, "No authenticated user found")
            return
        }

        Log.d(TAG, "Setting up Firestore listener for user: $userId")

        firestore.collection("chats")
            .whereArrayContains("userIds", userId)
            .addSnapshotListener { snapshot, error ->
                Log.d(TAG, "Firestore listener triggered")

                if (error != null) {
                    Log.e(TAG, "Error in Firestore listener: ${error.message}", error)
                    return@addSnapshotListener
                }

                if (snapshot == null) {
                    Log.e(TAG, "Snapshot is null")
                    return@addSnapshotListener
                }

                Log.d(TAG, "Snapshot received with ${snapshot.documents.size} documents")

                // Log each document
                snapshot.documents.forEachIndexed { index, doc ->
                    Log.d(TAG, "Document $index ID: ${doc.id}")
                    Log.d(TAG, "Document $index data: ${doc.data}")
                }

                viewModelScope.launch {
                    try {
                        val chats = snapshot.documents.mapNotNull { doc ->
                            try {
                                val chat = doc.toObject(ChatModel::class.java)
                                Log.d(TAG, "Parsed chat from doc ${doc.id}: $chat")
                                chat?.copy(chatId = doc.id)
                            } catch (e: Exception) {
                                Log.e(TAG, "Error parsing chat from doc ${doc.id}: ${e.message}", e)
                                null
                            }
                        }

                        Log.d(TAG, "Successfully parsed ${chats.size} chats")
                        val previews = mutableListOf<ChatPreviewModel>()

                        for ((index, chat) in chats.withIndex()) {
                            Log.d(TAG, "Processing chat $index: ${chat.chatId}")
                            Log.d(TAG, "Chat lawyer ID: ${chat.lawyerId}")

                            val lawyerId = chat.lawyerId.toString()
                            Log.d(TAG, "Looking for lawyer with ID: $lawyerId")

                            val lawyerDoc = try {
                                val querySnapshot = firestore.collection("lawyers")
                                    .whereEqualTo("id", chat.lawyerId)
                                    .get()
                                    .await()
                                querySnapshot.documents.firstOrNull()
//                                Log.d(TAG, "Lawyer document exists: ${doc.exists()}")
//                                if (doc.exists()) {
//                                    Log.d(TAG, "Lawyer document data: ${doc.data}")
//                                } else {
//                                    Log.w(TAG, "Lawyer document $lawyerId does not exist")
//                                }
//                                doc
                            } catch (e: Exception) {
                                Log.e(TAG, "Error fetching lawyer $lawyerId: ${e.message}", e)
                                null
                            }

                            val lawyer = try {
                                lawyerDoc?.toObject(LawyerModel::class.java)
                            } catch (e: Exception) {
                                Log.e(
                                    TAG,
                                    "Error parsing lawyer from doc $lawyerId: ${e.message}",
                                    e
                                )
                                null
                            }

                            if (lawyer != null) {
                                Log.d(TAG, "Successfully parsed lawyer: ${lawyer.name}")
                                previews.add(ChatPreviewModel(chat = chat, lawyer = lawyer))
                            } else {
                                Log.w(TAG, "Failed to get lawyer for ID: $lawyerId")
                            }
                        }

                        Log.d(TAG, "Final previews count: ${previews.size}")
                        _chatPreviews.value = previews
                        Log.d(TAG, "StateFlow updated with ${previews.size} previews")

                    } catch (e: Exception) {
                        Log.e(TAG, "Error in coroutine: ${e.message}", e)
                    }
                }
            }
    }

}



//    private val _chatList = MutableStateFlow<List<ChatModel>>(emptyList())
//    private val _lawyerList = MutableStateFlow<List<LawyerModel>>(emptyList())
//
//    val chatPreviews: StateFlow<List<ChatPreviewModel>> = combine(
//        _chatList, _lawyerList
//    ) { chats, lawyers ->
//        chats.mapNotNull { chat ->
//            val matchedLawyer = lawyers.find { it.id == chat.lawyerId }
//            matchedLawyer?.let { lawyer ->
//                ChatPreviewModel(chat = chat, lawyer = lawyer)
//            }
//        }
//    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // buat debug
//    init {
//        _chatList.value = listOf(
//            ChatModel(
//                chatId = "chat1",
//                userIds = listOf("user1", "lawyer1"),
//                lastMessageText = "Halo, bagaimana kabarnya?",
//                lastMessageTimestamp = Timestamp.now(),
//                lawyerId = 1,
//                qualification = "Hukum Keluarga"
//            ),
//            ChatModel(
//                chatId = "chat2",
//                userIds = listOf("user2", "lawyer2"),
//                lastMessageText = "Terima kasih atas bantuan Anda.",
//                lastMessageTimestamp = Timestamp.now(),
//                lawyerId = 2,
//                qualification = "Hukum Pidana"
//            )
//        )
//
//        _lawyerList.value = listOf(
//            LawyerModel(
//                id = 1,
//                name = "Reza Simanjuntak, S.H., M.H.",
//                qualifications = "Hukum Keluarga",
//                experience = "10 tahun",
//                cases = "Perceraian, Sengketa Tanah",
//                organization = "Lembaga Bantuan Hukum Jakarta",
//                rating = 4.8f,
//                reviewCount = 124,
//                imageUrl = "https://picsum.photos/200"
//            ),
//            LawyerModel(
//                id = 2,
//                name = "Maria Susanti, S.H., M.Kn.",
//                qualifications = "Hukum Pidana",
//                experience = "8 tahun",
//                cases = "Kasus Kriminal",
//                organization = "Kantor Pengacara Jakarta",
//                rating = 4.6f,
//                reviewCount = 80,
//                imageUrl = "https://picsum.photos/201"
//            )
//        )
//    }
