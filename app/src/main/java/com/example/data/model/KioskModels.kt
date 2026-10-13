package com.example.data.model

import java.util.UUID

enum class DocumentSource {
    SAMPLE_GOVERNMENT_FORM,
    SAMPLE_IC_CARD,
    SAMPLE_CERTIFICATE,
    SAMPLE_RESUME,
    CAMERA_SCAN,
    DEVICE_FILE
}

enum class PaperSize(val displayName: String, val basePriceMultiplier: Double) {
    A4("A4 (Standard)", 1.0),
    A3("A3 (Poster/Lukisan)", 2.0),
    LEGAL("Legal / F4 (Dokumen Rasmi)", 1.2)
}

enum class ScanFilter(val displayName: String) {
    ORIGINAL("Asal (Original Color)"),
    PHOTOSTAT_BW("Fotostat Hitam & Putih Berkontras Tinggi"),
    GRAYSCALE("Skala Kelabu Bersih (Grayscale)"),
    ENHANCED_SHARP("Warna Dipertingkat (Sharpened Color)")
}

data class PrintJob(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val source: DocumentSource,
    val totalPages: Int = 1,
    val copies: Int = 1,
    val isColor: Boolean = false,
    val paperSize: PaperSize = PaperSize.A4,
    val isDuplex: Boolean = false,
    val scanFilter: ScanFilter = ScanFilter.ORIGINAL,
    val pricePerUnit: Double = 0.20,
    val totalPrice: Double = 0.20,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "QUEUED"
)

enum class EWalletProvider(val accountName: String, val accountNumber: String, val bankTitle: String) {
    CIMB_DUITNOW("NASADEF SDN BHD", "7016657934", "CIMB Bank Berhad"),
    TNG_DUITNOW("NASADEF KIOSK SELF-SERVICE", "170997196374", "Touch 'n Go eWallet / DuitNow QR")
}

enum class PaymentStatus {
    IDLE,
    WAITING_SCAN,
    VERIFYING,
    SUCCESS,
    FAILED
}

data class PaymentTransaction(
    val transactionId: String = "DUITNOW-" + (100000..999999).random(),
    val provider: EWalletProvider,
    val amount: Double,
    val timestamp: Long = System.currentTimeMillis(),
    val status: PaymentStatus = PaymentStatus.WAITING_SCAN,
    val referenceNo: String = "REF" + System.currentTimeMillis().toString().takeLast(8)
)

data class MedicationReminder(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val dosage: String,
    val timeString: String,
    val instruction: String = "Selepas Makan",
    val isEnabled: Boolean = true
)

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: ChatSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ChatSender {
    USER,
    AI,
    SYSTEM
}

data class KioskLocationInfo(
    val kioskId: String = "NASADEF-KIOSK-KL04",
    val venueName: String = "Pusat Komuniti & UTC Sentral",
    val address: String = "Lot G-18, Aras Bawah, UTC Sentral, 50400 Kuala Lumpur",
    val latitude: Double = 3.1478,
    val longitude: Double = 101.6953,
    val helpline: String = "017-0997196374",
    val emergencyHotline: String = "999"
)
