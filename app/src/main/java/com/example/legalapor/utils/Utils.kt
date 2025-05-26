package com.example.legalapor.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

    fun formatDate(timestamp: Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("d MMM", Locale("id", "ID"))
        return formatter.format(date)
    }