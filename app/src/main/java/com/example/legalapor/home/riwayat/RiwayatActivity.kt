package com.example.legalapor.home.riwayat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.legalapor.home.riwayat.ui.theme.LegaLaporTheme

//class RiwayatActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            LegaLaporTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//                }
//            }
//        }
//    }
//}

@Composable
fun RiwayatScreen() {
    Text(text = "Riwayat")

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    LegaLaporTheme {
        RiwayatScreen()
    }
}