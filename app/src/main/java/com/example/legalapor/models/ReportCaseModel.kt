package com.example.legalapor.models

data class ReportCaseModel(
    // "Kepada" - This might be pre-filled or selected from another screen
    val lawyerNameToReportTo: String = "",

    // Judul Laporan
    val reportTitle: String = "",

    // Pelapor (Reporter)
    val reporterFullName: String = "",
    val reporterKtpNumber: String = "",
    val reporterAddress: String = "",
    val reporterPhoneNumber: String = "",

    // Hal yang dilaporkan (Reported Matters)
    val reportedCrimeType: String = "", // e.g., "Tindak pidana"
    val incidentDate: String = "",      // e.g., "dd/mm/yy"
    val incidentTime: String = "",      // e.g., "hh:mm WIB"
    val incidentLocation: String = "",
    val incidentDescription: String = "",

    // Terlapor (Reported Person)
    val reportedPersonFullName: String = "",
    val reportedPersonDescription: String = "",
    val reportedPersonAddress: String = "",
    val reportedPersonRelationship: String = "",
)
