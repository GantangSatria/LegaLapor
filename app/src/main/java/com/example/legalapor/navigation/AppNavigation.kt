package com.example.legalapor.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.legalapor.AuthScreen
import com.example.legalapor.home.beranda.ui.BerandaScreen
import com.example.legalapor.home.riwayat.RiwayatScreen
import com.example.legalapor.home.ui.MainScreen
import com.example.legalapor.laporan.LawyerSelectPage
import com.example.legalapor.laporan.ReportCasePage
import java.net.URLDecoder

@Composable
fun AppNavigation(navController: NavHostController) {
//    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Auth.route
    ) {
        composable(NavRoutes.Beranda.route) { MainScreen(navController) }
        composable(NavRoutes.Forum.route) { MainScreen(navController) }
        composable(NavRoutes.Riwayat.route) { MainScreen(navController) }
        composable(NavRoutes.Lainnya.route) { MainScreen(navController) }

        composable(NavRoutes.LawyerSelect.route) { LawyerSelectPage(navController = navController) }

        composable(
            route = NavRoutes.ReportCase.route,
            arguments = listOf(
                navArgument("lawyerId") { type = NavType.StringType },
                navArgument("lawyerName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lawyerId = backStackEntry.arguments?.getString("lawyerId") ?: ""
            val lawyerName = URLDecoder.decode(backStackEntry.arguments?.getString("lawyerName") ?: "", "UTF-8")

            ReportCasePage(
                lawyerId = lawyerId,
                lawyerName = lawyerName,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(NavRoutes.Auth.route) {
            AuthScreen(navController = navController)
        }

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
