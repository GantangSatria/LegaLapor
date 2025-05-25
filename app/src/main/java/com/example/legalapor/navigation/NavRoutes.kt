package com.example.legalapor.navigation

import android.net.Uri

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
    object Beranda : NavRoutes("beranda")
    object Forum : NavRoutes("forum")
    object Riwayat : NavRoutes("riwayat")
    object Lainnya : NavRoutes("lainnya")
    object LawyerSelect : NavRoutes("lawyer_select")
    object ReportCase : NavRoutes("reportCase/{lawyerId}/{lawyerName}") {
        fun createRoute(lawyerId: String, lawyerName: String) =
            "reportCase/$lawyerId/${Uri.encode(lawyerName)}"
    }
}