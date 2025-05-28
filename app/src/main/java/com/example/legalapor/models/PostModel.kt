package com.example.legalapor.models

data class PostModel(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorImageUrl: String = "",
    val content: String = "",
    val imageUrls: List<String> = emptyList(),
    val likes: Int = 0,
    val likedBy: List<String> = emptyList(),
    val comments: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)