package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import com.example.legalapor.models.ChatModel
import com.example.legalapor.models.MessageModel
import com.example.legalapor.models.ReportCaseModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

open class ReportCaseViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _uiState = MutableStateFlow(ReportCaseModel())
    val uiState: StateFlow<ReportCaseModel> = _uiState.asStateFlow()

    private val _reportSubmitted = MutableStateFlow(false)
    val reportSubmitted: StateFlow<Boolean> = _reportSubmitted

    fun setLawyerId(id: String) {
        _uiState.value = _uiState.value.copy(lawyerId = id)
    }

    fun updateLawyerNameToReportTo(name: String) {
        _uiState.update { it.copy(lawyerNameToReportTo = name) }
    }

    fun updateReportTitle(title: String) {
        _uiState.update { it.copy(reportTitle = title) }
    }

    fun updateReporterFullName(name: String) {
        _uiState.update { it.copy(reporterFullName = name) }
    }

    fun updateReporterKtpNumber(ktp: String) {
        _uiState.update { it.copy(reporterKtpNumber = ktp) }
    }

    fun updateReporterAddress(address: String) {
        _uiState.update { it.copy(reporterAddress = address) }
    }

    fun updateReporterPhoneNumber(phone: String) {
        _uiState.update { it.copy(reporterPhoneNumber = phone) }
    }

    fun updateReportedCrimeType(crimeType: String) {
        _uiState.update { it.copy(reportedCrimeType = crimeType) }
    }

    fun updateIncidentDate(date: String) {
        _uiState.update { it.copy(incidentDate = date) }
    }

    fun updateIncidentTime(time: String) {
        _uiState.update { it.copy(incidentTime = time) }
    }

    fun updateIncidentLocation(location: String) {
        _uiState.update { it.copy(incidentLocation = location) }
    }

    fun updateIncidentDescription(description: String) {
        _uiState.update { it.copy(incidentDescription = description) }
    }

    fun updateReportedPersonFullName(name: String) {
        _uiState.update { it.copy(reportedPersonFullName = name) }
    }

    fun updateReportedPersonDescription(description: String) {
        _uiState.update { it.copy(reportedPersonDescription = description) }
    }

    fun updateReportedPersonAddress(address: String) {
        _uiState.update { it.copy(reportedPersonAddress = address) }
    }

    fun updateReportedPersonRelationship(relationship: String) {
        _uiState.update { it.copy(reportedPersonRelationship = relationship) }
    }

//    fun updateDeclarationChecked(isChecked: Boolean) {
//        _uiState.update { it.copy(declarationChecked = isChecked) }
//    }

    fun submitReport() {
        println("Report Submitted: ${uiState.value}")

        _reportSubmitted.value = true
    }

//    fun getOrCreateChatWithLawyer(lawyerId: Int, onResult: (chatId: String) -> Unit) {
//        val currentUserId = auth.currentUser?.uid.toString()
//
//        firestore.collection("chats")
//            .whereArrayContains("userIds", currentUserId)
//            .get()
//            .addOnSuccessListener { querySnapshot ->
//                val chat = querySnapshot.documents.find {
//                    it.get("lawyerId") == lawyerId
//                }
//                if (chat != null) {
//                    // Chat exists, return existing chatId
//                    onResult(chat.id)
//                } else {
//                    // Create new chat
//                    val newChat = ChatModel(
//                        chatId = "",
//                        userIds = listOf(currentUserId, lawyerId.toString()),
//                        lawyerId = lawyerId,
//                        lastMessageText = "Start chatting…"
//                    )
//                    val docRef = firestore.collection("chats").document()
//                    docRef.set(newChat.copy(chatId = docRef.id))
//                        .addOnSuccessListener {
//                            onResult(docRef.id)
//                        }
//                }
//            }
//    }

    fun getOrCreateChatId(userId: String, lawyerId: Int, onResult: (String) -> Unit) {
        firestore.collection("chats")
            .whereEqualTo("lawyerId", lawyerId)
            .whereArrayContains("userIds", userId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val chatId = result.documents[0].id
                    onResult(chatId)
                } else {
                    val newChatRef = firestore.collection("chats").document()
                    val chat = ChatModel(
                        chatId = newChatRef.id,
                        userIds = listOf(userId, lawyerId.toString()),
                        lastMessageText = "",
                        lastMessageTimestamp = Timestamp.now(),
                        lawyerId = lawyerId,
                        qualification = ""
                    )
                    newChatRef.set(chat).addOnSuccessListener {
                        onResult(chat.chatId)
                    }
                }
            }
    }

    fun sendInitialMessageIfNeeded(chatId: String) {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val message = MessageModel(
            messageId = UUID.randomUUID().toString(),
            senderId = currentUserId,
            receiverId = "",
            message = "Halo, saya baru saja melaporkan kasus kepada Anda.",
            timestamp = Timestamp.now()
        )

        val messagesRef = firestore.collection("chats").document(chatId).collection("messages")
        messagesRef.document(message.messageId).set(message)

        firestore.collection("chats").document(chatId)
            .update(
                mapOf(
                    "lastMessageText" to message.message,
                    "lastMessageTimestamp" to message.timestamp
                )
            )
    }


}