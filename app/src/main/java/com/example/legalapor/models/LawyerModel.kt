package com.example.legalapor.models

data class LawyerModel(val id: Int,
                       val name: String,
                       val qualifications: String,
                       val experience: String,
                       val cases: String,
                       val organization: String,
                       val rating: Float,
                       val reviewCount: Int,
                       val imageRes: Int)
