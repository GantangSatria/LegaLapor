package com.example.legalapor.service.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.legalapor.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class UserViewModel : ViewModel() {
    private val _user = mutableStateOf<UserModel?>(null)
    val user: State<UserModel?> = _user

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        val uid = Firebase.auth.currentUser?.uid
        if (uid != null) {
            Firebase.firestore.collection("users").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val userData = document.toObject(UserModel::class.java)
                        _user.value = userData
                    }
                }
        }
    }
}