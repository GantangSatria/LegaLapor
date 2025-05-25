package com.example.legalapor.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
    object Beranda : NavRoutes("beranda")
    object Forum : NavRoutes("forum")
    object Riwayat : NavRoutes("riwayat")
    object Lainnya : NavRoutes("lainnya")
    object LawyerSelect : NavRoutes("lawyer_select")
}