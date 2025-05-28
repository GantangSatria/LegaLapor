package com.example.legalapor.home.beranda.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.legalapor.R
import com.example.legalapor.home.beranda.ui.components.LawyerProfileCard
import com.example.legalapor.home.beranda.ui.theme.LegaLaporTheme
import com.example.legalapor.models.LawyerModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.legalapor.service.viewmodel.BerandaViewModel



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

val chipItems = listOf("Semua") + listOf(
    "Pidana",
    "Perdata",
    "Pajak",
    "Kepailitan",
    "Perkawinan dan Perceraian",
    "Pertanahan",
    "Tenaga Kerja",
    "Hak Cipta"
)
// nuat preview
var lawyerItems = listOf(LawyerModel(1, "Farida Choirunnisa, S.H.", "", "Pengalaman 3 Tahun", "220 Kasus", "PBH Peradi", 4.5f, 38, "https://qqwnyvosdtoosydrtdmx.supabase.co/storage/v1/object/public/lawyer-image//StockCake-Lawyer%20holding%20book_1725547095%201.png"),
    LawyerModel(2, "Zayn Maliki Al-Hasc, S.H., M.H.", "", "Pengalaman 6 Tahun", "110 Kasus", "YLBHI", 4.8f, 52, ""),
    LawyerModel(3, "Aisha Putri, S.H.", "", "Pengalaman 5 Tahun", "180 Kasus", "LBH Jakarta", 4.6f, 45, ""));

@Composable
fun BerandaScreen(viewModel: BerandaViewModel = viewModel()) {
    val lawyers by viewModel.lawyers.collectAsState()
    // var activeChipLabel by remember { mutableStateOf("Pidana") }
    var activeChipLabel by remember { mutableStateOf("Semua") }

    val displayedLawyers = remember(lawyers, activeChipLabel) {
        if (activeChipLabel == "Semua") lawyers
        else lawyers.filter { it.qualifications == activeChipLabel }
    }

    Scaffold {
        Column(modifier = Modifier.padding(horizontal = 16.dp).padding(it)) {
            Image(painter = painterResource(id = R.drawable.home_hero_image), contentDescription = null, Modifier.height(151.dp).width(419.dp))

            Spacer(modifier = Modifier.height(28.dp))

            Text(text = "Pengacara", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
//            Spacer(modifier = Modifier.height(13.dp))

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chipItems) { chipText ->
                    val isSelected = activeChipLabel == chipText
                    val chipEnabled = true
                    ElevatedFilterChip(
                        selected = isSelected,
                        onClick = { activeChipLabel = chipText },
                        label = {
                            Text(chipText)},
                        enabled = chipEnabled,
                        colors = FilterChipDefaults.elevatedFilterChipColors(
                            containerColor = Color.White,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            selectedContainerColor = Color(0xFF4285F4),
                            selectedLabelColor = Color.White,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = Color.Black,
                            selectedBorderColor = Color.Transparent,
                            borderWidth = 1.dp,
                            selectedBorderWidth = 1.dp,
                            enabled = chipEnabled,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            LazyRow (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(displayedLawyers) { lawyer ->
                    LawyerProfileCard(lawyer = lawyer)
                }
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

//            Text(text = "Pilih Kategori", fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BerandaScreen(viewModel = BerandaViewModel())
//}