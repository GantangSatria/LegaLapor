package com.example.legalapor.splashscreen

import androidx.compose.runtime.*
import com.example.legalapor.home.ui.MainScreen
import com.example.legalapor.splashscreen.WelcomeScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun AppEntryPoint() {
    var showOnboarding by remember { mutableStateOf(true) }

    if (showOnboarding) {
        WelcomeScreen(
            onCreateAccount = { /* aksi buat akun */ },
            onLogin = { /* aksi login */ },
            onSkip = { /* aksi skip onboarding */ }
        )
    } else {
        MainScreen(navController = rememberNavController())
    }
}
