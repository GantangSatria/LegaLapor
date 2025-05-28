package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.legalapor.models.PostModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val _posts = MutableStateFlow<List<PostModel>>(emptyList())
    val posts: StateFlow<List<PostModel>> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        db.collection("posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener { value, _ ->
                val postList = value?.documents?.mapNotNull { it.toObject(PostModel::class.java) } ?: emptyList()
                _posts.value = postList
            }
    }

    fun addPost(post: PostModel) {
        val newPostRef = db.collection("posts").document()
        val postWithId = post.copy(id = newPostRef.id)
        newPostRef.set(postWithId)
    }

    fun toggleLike(postId: String, userId: String, like: Boolean) {
        val postRef = Firebase.firestore.collection("posts").document(postId)

        Firebase.firestore.runTransaction { transaction ->
            val snapshot = transaction.get(postRef)

            val currentLikes = snapshot.getLong("likes")?.toInt() ?: 0
            val currentLikedBy = snapshot.get("likedBy") as? List<String> ?: emptyList()

            val updatedLikes = if (like) currentLikes + 1 else currentLikes - 1
            val updatedLikedBy = if (like) currentLikedBy + userId else currentLikedBy - userId

            transaction.update(postRef, mapOf(
                "likes" to updatedLikes,
                "likedBy" to updatedLikedBy
            ))
        }
    }
}