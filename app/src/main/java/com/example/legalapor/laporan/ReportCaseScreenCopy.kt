package com.example.legalapor.laporan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.legalapor.laporan.ui.theme.PrimaryBlueLaporan
import com.example.legalapor.laporan.ui.theme.KepadaBox
import com.example.legalapor.laporan.ui.theme.GreyBox
import com.example.legalapor.service.viewmodel.ReportCaseViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportCasePage(

    viewModel: ReportCaseViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    lawyerId: String,
    lawyerName: String
) {
    val uiState by viewModel.uiState.collectAsState()
    var declarationCheckedLocally by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.updateLawyerNameToReportTo(lawyerName)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Laporan Kasus",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlueLaporan
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = PrimaryBlueLaporan
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        backgroundColor = GreyBox
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Kepada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kepada",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    modifier = Modifier.weight(0.3f)
                )

                Text(
                    text = ":",
                    fontSize = 16.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .height(40.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                        .background(
                            color = KepadaBox,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE5E7EB),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = uiState.lawyerNameToReportTo,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 12.dp)
                    )
                }
            }

            // Judul Card
            SectionCardTitleOnly(
                title = "Judul",
                value = uiState.reportTitle,
                onValueChange = viewModel::updateReportTitle
            )

            // Pelapor Card
            SectionCard(
                title = "Pelapor",
                titleColor = PrimaryBlueLaporan
            ) {
                FormField(
                    label = "1. Nama Lengkap",
                    value = uiState.reporterFullName,
                    onValueChange = viewModel::updateReporterFullName
                )
                FormField(
                    label = "2. Nomor (KTP)",
                    value = uiState.reporterKtpNumber,
                    onValueChange = viewModel::updateReporterKtpNumber,
                    keyboardType = KeyboardType.Number
                )
                FormField(
                    label = "3. Alamat",
                    value = uiState.reporterAddress,
                    onValueChange = viewModel::updateReporterAddress,
                    maxLines = 3,
                    minLines = 3
                )
                FormField(
                    label = "4. No Telepon",
                    value = uiState.reporterPhoneNumber,
                    onValueChange = viewModel::updateReporterPhoneNumber,
                    keyboardType = KeyboardType.Phone
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hal yang dilaporkan Card
            SectionCard(
                title = "Hal yang dilaporkan",
                titleColor = PrimaryBlueLaporan
            ) {
                FormField(
                    label = "1. Tindak pidana",
                    value = uiState.reportedCrimeType,
                    onValueChange = viewModel::updateReportedCrimeType
                )
                FormField(
                    label = "2. Tanggal Kejadian",
                    value = uiState.incidentDate,
                    onValueChange = viewModel::updateIncidentDate,
                    placeholder = "dd / mm / yyyy"
                )
                FormField(
                    label = "3. Waktu Kejadian",
                    value = uiState.incidentTime,
                    onValueChange = viewModel::updateIncidentTime,
                    placeholder = "-- : -- Senin, 09:00 WIB"
                )
                FormField(
                    label = "4. Lokasi Kejadian",
                    value = uiState.incidentLocation,
                    onValueChange = viewModel::updateIncidentLocation
                )
                FormField(
                    label = "5. Deskripsi Kejadian",
                    value = uiState.incidentDescription,
                    onValueChange = viewModel::updateIncidentDescription,
                    maxLines = 5,
                    minLines = 3
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Terlapor Card
            SectionCard(
                title = "Terlapor",
                titleColor = PrimaryBlueLaporan
            ) {
                FormField(
                    label = "1. Nama Lengkap",
                    value = uiState.reportedPersonFullName,
                    onValueChange = viewModel::updateReportedPersonFullName
                )
                FormField(
                    label = "2. Deskripsi Pelaku",
                    value = uiState.reportedPersonDescription,
                    onValueChange = viewModel::updateReportedPersonDescription
                )
                FormField(
                    label = "3. Alamat Pelaku",
                    value = uiState.reportedPersonAddress,
                    onValueChange = viewModel::updateReportedPersonAddress,
                    minLines = 3
                )
                FormField(
                    label = "4. Hubungan dengan pelapor",
                    value = uiState.reportedPersonRelationship,
                    onValueChange = viewModel::updateReportedPersonRelationship
                )
                Text(
                    "* tulis [-] jika tidak mengenal identitas pelaku.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Declaration Checkbox
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
            ) {
                Checkbox(
                    checked = declarationCheckedLocally,
                    onCheckedChange = { declarationCheckedLocally = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF4CAF50),
                        uncheckedColor = Color(0xFF9CA3AF),
                        checkmarkColor = Color.White
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Saya menyatakan bahwa laporan ini dibuat dengan sebenarnya dan dapat dipertanggungjawabkan di depan hukum.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Submit Button
            Button(
                onClick = { viewModel.submitReport() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = declarationCheckedLocally,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = PrimaryBlueLaporan,
                    disabledContainerColor = Color(0xFFBBC3D4)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Kirim",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SectionCardTitleOnly(
    title: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(
                            color = GreyBox,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE5E7EB),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = Color.Black
                        ),
                        singleLine = true
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp))
                .background(
                    color = PrimaryBlueLaporan,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun SectionCard(
    title: String,
    titleColor: Color = PrimaryBlueLaporan,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 16.dp)
            ) {
                content()
                Text(
                    text = "Belum disimpan",
                    fontSize = 12.sp,
                    color = Color(0xFF9CA3AF),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }

        Box(
            modifier = Modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(10.dp))
                .background(
                    color = PrimaryBlueLaporan,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun FormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLines: Int = 1,
    minLines: Int = 1,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF333333),
            modifier = Modifier.weight(0.4f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = ":",
            fontSize = 14.sp,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Box(
            modifier = Modifier
                .weight(0.6f)
                .height(30.dp)
                .background(
                    color = GreyBox,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFFE5E7EB),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                maxLines = maxLines,
                minLines = minLines,
                singleLine = maxLines == 1,
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && placeholder != null) {
                        Text(
                            text = placeholder,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterStart)
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

class FakeReportCaseViewModel : ReportCaseViewModel() {
    init {
        updateLawyerNameToReportTo("Reza Simanjuntak, S.H, M.H.")
    }
}

@Preview(showBackground = true)
@Composable
fun ReportCaseScreenPreview() {
    ReportCasePage(
        viewModel = FakeReportCaseViewModel(),
        onNavigateBack = {},
        lawyerId = "",
        lawyerName = "febro"
    )
}