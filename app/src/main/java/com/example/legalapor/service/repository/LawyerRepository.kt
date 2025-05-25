package com.example.legalapor.service.repository

import com.example.legalapor.models.LawyerModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LawyerRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getLawyers(): List<LawyerModel> {
        return try {
            val snapshot = db.collection("lawyers").get().await()
            snapshot.documents.mapNotNull { it.toObject(LawyerModel::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
