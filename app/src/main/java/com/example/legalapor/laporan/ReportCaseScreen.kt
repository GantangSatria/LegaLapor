package com.example.legalapor.laporan

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowColumnScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.legalapor.navigation.NavRoutes
import com.example.legalapor.service.viewmodel.ReportCaseViewModel
import com.google.firebase.auth.FirebaseAuth
import java.net.URLEncoder

class ReportCaseScreen {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCasePage(
    navController: NavHostController,
    viewModel: ReportCaseViewModel = viewModel(),
    onNavigateBack: () -> Unit = { navController.popBackStack() },
    lawyerId: String,
    lawyerName: String
) {
    val uiState by viewModel.uiState.collectAsState()
    var declarationCheckedLocally by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.updateLawyerNameToReportTo(lawyerName)
        viewModel.setLawyerId(lawyerId)
    }

    val reportSubmitted by viewModel.reportSubmitted.collectAsState()
    var chatId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(chatId) {
        chatId?.let {
            val encodedLawyerName = URLEncoder.encode(lawyerName, "UTF-8")
            navController.navigate("chat/$it/$lawyerId/$encodedLawyerName")
        }
    }

    val currentUser = FirebaseAuth.getInstance().currentUser?.uid ?: return

    fun handleSubmit(navController: NavHostController) {
        viewModel.submitReport()
        viewModel.getOrCreateChatId(currentUser, lawyerId.toInt()) { generatedChatId ->
            viewModel.sendInitialMessageIfNeeded(generatedChatId)
            chatId = generatedChatId

            navController.navigate("chat/$generatedChatId/$lawyerId/$lawyerName") {
                launchSingleTop = true
                popUpTo(NavRoutes.Beranda.route) {
                    inclusive = false
                }

            }
        }
    }


    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Laporan Kasus", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF31469F))
                        },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors( // Use centerAlignedTopAppBarColors
                    containerColor = Color.White,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = Color(0xFF31469F)
                )
            )
        },
        modifier = Modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionTitle("Kepada :")
            OutlinedTextField(
                value = uiState.lawyerNameToReportTo,
                onValueChange = { viewModel.updateLawyerNameToReportTo(it) },
                label = { Text("Nama Pengacara") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Judul Section
            Box(modifier = Modifier
                .border(shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, color = Color.White))
                .shadow(elevation = 2.dp)
                .padding(all = 13.dp)
            ) {
                OutlinedTextField(
                    value = uiState.reportTitle,
                    onValueChange = { viewModel.updateReportTitle(it) },
                    label = { Text("Judul") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pelapor Section
            SectionTitle("Pelapor")
            FormTextField(label = "1. Nama Lengkap", value = uiState.reporterFullName, onValueChange = viewModel::updateReporterFullName)
            FormTextField(label = "2. Nomor (KTP)", value = uiState.reporterKtpNumber, onValueChange = viewModel::updateReporterKtpNumber, keyboardType = KeyboardType.Number)
            FormTextField(label = "3. Alamat", value = uiState.reporterAddress, onValueChange = viewModel::updateReporterAddress, maxLines = 3)
            FormTextField(label = "4. No Telepon", value = uiState.reporterPhoneNumber, onValueChange = viewModel::updateReporterPhoneNumber, keyboardType = KeyboardType.Phone)
            Spacer(modifier = Modifier.height(24.dp))

            // Hal yang dilaporkan Section
            SectionTitle("Hal yang dilaporkan")
            FormTextField(label = "1. Tindak pidana", value = uiState.reportedCrimeType, onValueChange = viewModel::updateReportedCrimeType)
            FormTextField(label = "2. Tanggal Kejadian", value = uiState.incidentDate, onValueChange = viewModel::updateIncidentDate, placeholder = "dd/mm/yyyy")
            FormTextField(label = "3. Waktu Kejadian", value = uiState.incidentTime, onValueChange = viewModel::updateIncidentTime, placeholder = "hh:mm WIB")
            FormTextField(label = "4. Lokasi Kejadian", value = uiState.incidentLocation, onValueChange = viewModel::updateIncidentLocation)
            FormTextField(label = "5. Deskripsi Kejadian", value = uiState.incidentDescription, onValueChange = viewModel::updateIncidentDescription, maxLines = 5, minLines = 3)
            Spacer(modifier = Modifier.height(24.dp))

            // Terlapor Section
            SectionTitle("Terlapor")
            FormTextField(label = "1. Nama Lengkap", value = uiState.reportedPersonFullName, onValueChange = viewModel::updateReportedPersonFullName)
            FormTextField(label = "2. Deskripsi Pelaku", value = uiState.reportedPersonDescription, onValueChange = viewModel::updateReportedPersonDescription)
            FormTextField(label = "3. Alamat Pelaku", value = uiState.reportedPersonAddress, onValueChange = viewModel::updateReportedPersonAddress, maxLines = 3)
            FormTextField(label = "4. Hubungan dengan pelapor", value = uiState.reportedPersonRelationship, onValueChange = viewModel::updateReportedPersonRelationship)
            Text("* tulis [-] jika tidak mengenali identitas pelaku.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))


            // Declaration Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = declarationCheckedLocally,
                    onCheckedChange = { declarationCheckedLocally = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Saya menyatakan bahwa laporan ini dibuat dengan sebenarnya dan dapat dipertanggungjawabkan di depan hukum.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            Button(
                onClick = { handleSubmit(navController) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = declarationCheckedLocally
            ) {
                Text("Kirim", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    minLines: Int = 1,
    readOnly: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { {Text(it)} },
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = maxLines,
        minLines = minLines,
        readOnly = readOnly,
//        colors = Color(0xFFF4F5F7)
    )
}

class FakeReportCaseViewModel : ReportCaseViewModel() {
    init {
        updateLawyerNameToReportTo("Reza Simanjuntak, S.H, M.H.")
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ReportCaseScreenPreview() {
//        ReportCasePage(viewModel = FakeReportCaseViewModel(),
//            onNavigateBack = {},
//            lawyerId = "",
//            lawyerName = "febro"
//        )
//}