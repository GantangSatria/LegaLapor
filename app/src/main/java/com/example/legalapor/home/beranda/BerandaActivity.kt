package com.example.legalapor.home.beranda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.legalapor.R
import com.example.legalapor.home.beranda.ui.theme.LegaLaporTheme

class BerandaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LegaLaporTheme {
                BerandaScreen()
            }
        }
    }
}

@Composable
fun BerandaScreen() {
    Scaffold {
        Column(modifier = Modifier.padding(16.dp).padding(it)) {
            Image(painter = painterResource(id = R.drawable.home_hero_image), contentDescription = null, Modifier.height(151.dp).width(419.dp))
            Text(text = "Beranda")
            Text(text = "tes")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BerandaScreen()
}