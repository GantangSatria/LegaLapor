package com.example.legalapor.models

data class LawyerModel(
    val id: Int = 0,
    val name: String = "",
    val qualifications: String = "",
    val experience: String = "",
    val cases: String = "",
    val organization: String = "",
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val imageUrl: String = ""
)
