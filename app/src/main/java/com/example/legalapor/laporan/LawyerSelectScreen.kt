package com.example.legalapor.laporan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.legalapor.home.ui.components.CustomTopAppBar
import com.example.legalapor.laporan.components.SelectableLawyerCard
import com.example.legalapor.laporan.ui.theme.LegaLaporTheme
import com.example.legalapor.models.LawyerModel
import com.example.legalapor.navigation.NavRoutes
import com.example.legalapor.service.viewmodel.LawyerSelectViewModel
import com.example.legalapor.service.viewmodel.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

//class LawyerSelectScreen : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            LegaLaporTheme {
//
//            }
//        }
//    }
//}

@Composable
fun LawyerSelectPage( navController: NavController, viewModel: LawyerSelectViewModel = viewModel(), userViewModel: UserViewModel = viewModel()) {
    val lawyers by viewModel.lawyers.collectAsState()
    Scaffold(
        topBar = {CustomTopAppBar(userName = userViewModel.name.value)},
        content = { innerPadding ->
           Box(
               modifier = Modifier
                   .padding(innerPadding)
                   .fillMaxSize()
                   .padding(16.dp)
           ) {
               LazyColumn (
                   modifier = Modifier.fillMaxSize(),
                   verticalArrangement = Arrangement.spacedBy(16.dp)
               ){
                   items(lawyers) { lawyer ->
                       SelectableLawyerCard(lawyer = lawyer) {
                           navController.navigate(NavRoutes.ReportCase.createRoute(lawyer.id.toString(), lawyer.name))
                       }
                   }
               }
           }
        }
    )
}



class FakeLawyerSelectViewModel : LawyerSelectViewModel() {
    override val lawyers: StateFlow<List<LawyerModel>> = MutableStateFlow(
        listOf(
            LawyerModel(1, "Farida Choirunnisa, S.H.", "", "Pengalaman 3 Tahun", "150 Kasus", "PBH Peradi", 4.5f, 38, "https://placehold.co/72x72/E0E0E0/B0B0B0?text=FC"),
            LawyerModel(2, "Zayn Maliki Al-Hasc, S.H., M.H.", "", "Pengalaman 6 Tahun", "80 Kasus", "YLBHI",4.8f, 52, "https://placehold.co/72x72/D1C4E9/7E57C2?text=ZM"),
            LawyerModel(3, "Aisha Putri, S.H.", "", "Pengalaman 5 Tahun", "120 Kasus", "LBH Jakarta", 4.6f, 45, "https://placehold.co/72x72/C8E6C9/66BB6A?text=AP"),
            LawyerModel(4, "Budi Santoso, S.H.", "", "Pengalaman 8 Tahun", "200 Kasus", "LBH APIK", 4.7f, 60, "https://placehold.co/72x72/BBDEFB/42A5F5?text=BS")
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LawyerSelectPagePreview() {
    LawyerSelectPage(
        viewModel = FakeLawyerSelectViewModel(),
        navController = NavController(context = LocalContext.current)
    )
}