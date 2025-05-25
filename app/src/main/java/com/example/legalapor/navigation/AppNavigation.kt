package com.example.legalapor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.legalapor.home.beranda.ui.BerandaScreen
import com.example.legalapor.home.riwayat.RiwayatScreen
import com.example.legalapor.home.ui.MainScreen
import com.example.legalapor.laporan.LawyerSelectPage

@Composable
fun AppNavigation(navController: NavHostController) {
//    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Beranda.route
    ) {
        composable(NavRoutes.Beranda.route) { MainScreen(navController) }
        composable(NavRoutes.Forum.route) { MainScreen(navController) }
        composable(NavRoutes.Riwayat.route) { MainScreen(navController) }
        composable(NavRoutes.Lainnya.route) { MainScreen(navController) }

        composable(NavRoutes.LawyerSelect.route) { LawyerSelectPage() }
    }


}

//@Composable
//fun HomeScreen(navController: NavHostController) {
//    MainScreen()
//}
//
//@Composable
//fun ReportScreen(navController: NavHostController) {
//    LawyerSelectPage()
//}

