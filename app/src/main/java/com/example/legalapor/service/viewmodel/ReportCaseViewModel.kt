package com.example.legalapor.service.viewmodel

import androidx.lifecycle.ViewModel
import com.example.legalapor.models.ReportCaseModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ReportCaseViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReportCaseModel())
    val uiState: StateFlow<ReportCaseModel> = _uiState.asStateFlow()

    fun updateLawyerNameToReportTo(name: String) {
        _uiState.update { it.copy(lawyerNameToReportTo = name) }
    }

    fun updateReportTitle(title: String) {
        _uiState.update { it.copy(reportTitle = title) }
    }

    fun updateReporterFullName(name: String) {
        _uiState.update { it.copy(reporterFullName = name) }
    }

    fun updateReporterKtpNumber(ktp: String) {
        _uiState.update { it.copy(reporterKtpNumber = ktp) }
    }

    fun updateReporterAddress(address: String) {
        _uiState.update { it.copy(reporterAddress = address) }
    }

    fun updateReporterPhoneNumber(phone: String) {
        _uiState.update { it.copy(reporterPhoneNumber = phone) }
    }

    fun updateReportedCrimeType(crimeType: String) {
        _uiState.update { it.copy(reportedCrimeType = crimeType) }
    }

    fun updateIncidentDate(date: String) {
        _uiState.update { it.copy(incidentDate = date) }
    }

    fun updateIncidentTime(time: String) {
        _uiState.update { it.copy(incidentTime = time) }
    }

    fun updateIncidentLocation(location: String) {
        _uiState.update { it.copy(incidentLocation = location) }
    }

    fun updateIncidentDescription(description: String) {
        _uiState.update { it.copy(incidentDescription = description) }
    }

    fun updateReportedPersonFullName(name: String) {
        _uiState.update { it.copy(reportedPersonFullName = name) }
    }

    fun updateReportedPersonDescription(description: String) {
        _uiState.update { it.copy(reportedPersonDescription = description) }
    }

    fun updateReportedPersonAddress(address: String) {
        _uiState.update { it.copy(reportedPersonAddress = address) }
    }

    fun updateReportedPersonRelationship(relationship: String) {
        _uiState.update { it.copy(reportedPersonRelationship = relationship) }
    }

//    fun updateDeclarationChecked(isChecked: Boolean) {
//        _uiState.update { it.copy(declarationChecked = isChecked) }
//    }

    fun submitReport() {
        println("Report Submitted: ${uiState.value}")
    }
}