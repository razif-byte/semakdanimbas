package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.DocumentSource
import com.example.data.model.KioskLocationInfo
import com.example.data.model.PaymentTransaction
import com.example.data.model.PrintJob
import com.example.services.GeminiChatService
import com.example.services.VoiceGuideManager
import com.example.ui.components.BiometricDialog
import com.example.ui.components.WatermarkFooterScaffold
import com.example.ui.screens.AiChatScreen
import com.example.ui.screens.DeviceDownloadQrScreen
import com.example.ui.screens.DocumentPrintScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.KioskDestination
import com.example.ui.screens.LocationSharingScreen
import com.example.ui.screens.MedicationReminderScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.PrintPreviewScreen
import com.example.ui.screens.PrintingProgressScreen
import com.example.ui.screens.ScannerScreen
import com.example.ui.screens.SosEmergencyScreen
import com.example.ui.theme.KioskTheme

class KioskViewModel : ViewModel() {
    var destination by mutableStateOf(KioskDestination.HOME)
    var isDarkMode by mutableStateOf(false)
    var isHighContrast by mutableStateOf(false)
    var isBigText by mutableStateOf(false)
    var isVoiceEnabled by mutableStateOf(true)

    var isLoggedIn by mutableStateOf(false)
    var userName by mutableStateOf("Pengguna Tetamu")

    var currentPrintJob by mutableStateOf(
        PrintJob(
            title = "Borang Permohonan Rasmi Kerajaan",
            source = DocumentSource.SAMPLE_GOVERNMENT_FORM,
            totalPages = 2,
            copies = 1,
            isColor = false,
            pricePerUnit = 0.20,
            totalPrice = 0.40
        )
    )

    var lastTransaction by mutableStateOf<PaymentTransaction?>(null)
    val locationInfo = KioskLocationInfo()
}

class MainActivity : ComponentActivity() {
    private var voiceGuideManager: VoiceGuideManager? = null
    private val geminiService = GeminiChatService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        voiceGuideManager = VoiceGuideManager(this)

        setContent {
            val vm: KioskViewModel = viewModel()
            var showBiometric by remember { mutableStateOf(false) }

            DisposableEffect(Unit) {
                onDispose {
                    voiceGuideManager?.shutdown()
                }
            }

            fun speakText(text: String) {
                if (vm.isVoiceEnabled) {
                    voiceGuideManager?.speak(text)
                }
            }

            KioskTheme(
                darkTheme = vm.isDarkMode,
                highContrast = vm.isHighContrast,
                bigText = vm.isBigText
            ) {
                // BackHandler to return to HOME from any secondary screen
                if (vm.destination != KioskDestination.HOME) {
                    BackHandler {
                        vm.destination = KioskDestination.HOME
                        speakText("Kembali ke Menu Utama.")
                    }
                }

                val title = when (vm.destination) {
                    KioskDestination.HOME -> "Kiosk Percetakan & Fotostat"
                    KioskDestination.DOCUMENT_PRINT -> "Cetak Dokumen & PDF"
                    KioskDestination.SCANNER -> "Pengimbas Resolusi Tinggi"
                    KioskDestination.PRINT_PREVIEW -> "Pratonton Cetakan"
                    KioskDestination.PAYMENT -> "Pembayaran Nirtunai DuitNow"
                    KioskDestination.PRINTING_PROGRESS -> "Proses Percetakan"
                    KioskDestination.MEDICATION_REMINDER -> "Peringatan Ubat Harian"
                    KioskDestination.SOS_EMERGENCY -> "Bantuan Kecemasan SOS"
                    KioskDestination.LOCATION_SHARING -> "Perkongsian Lokasi Keluarga"
                    KioskDestination.AI_CHAT -> "Sembang AI Langsung"
                    KioskDestination.DEVICE_DOWNLOAD_QR -> "Auto Download Kod QR"
                }

                WatermarkFooterScaffold(
                    title = title,
                    onBack = if (vm.destination != KioskDestination.HOME) {
                        {
                            vm.destination = KioskDestination.HOME
                            speakText("Kembali ke Menu Utama.")
                        }
                    } else null,
                    isDarkMode = vm.isDarkMode,
                    onToggleDarkMode = {
                        vm.isDarkMode = !vm.isDarkMode
                        speakText(if (vm.isDarkMode) "Mod gelap diaktifkan." else "Mod cerah diaktifkan.")
                    },
                    isHighContrast = vm.isHighContrast,
                    onToggleHighContrast = {
                        vm.isHighContrast = !vm.isHighContrast
                        speakText(if (vm.isHighContrast) "Mod kontras tinggi OKU diaktifkan." else "Mod kontras biasa diaktifkan.")
                    },
                    isBigText = vm.isBigText,
                    onToggleBigText = {
                        vm.isBigText = !vm.isBigText
                        speakText(if (vm.isBigText) "Mod teks besar warga emas diaktifkan." else "Mod teks saiz biasa diaktifkan.")
                    },
                    isVoiceEnabled = vm.isVoiceEnabled,
                    onToggleVoice = {
                        vm.isVoiceEnabled = !vm.isVoiceEnabled
                        voiceGuideManager?.isEnabled = vm.isVoiceEnabled
                        if (vm.isVoiceEnabled) {
                            voiceGuideManager?.speak("Panduan suara diaktifkan.")
                        } else {
                            voiceGuideManager?.stop()
                        }
                    },
                    onSosClick = {
                        vm.destination = KioskDestination.SOS_EMERGENCY
                        speakText("Kecemasan! Membuka skrin bantuan SOS satu sentuhan.")
                    }
                ) {
                    when (vm.destination) {
                        KioskDestination.HOME -> {
                            HomeScreen(
                                isLoggedIn = vm.isLoggedIn,
                                userName = vm.userName,
                                onNavigate = { dest -> vm.destination = dest },
                                onOpenBiometric = { showBiometric = true },
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.DOCUMENT_PRINT -> {
                            DocumentPrintScreen(
                                currentJob = vm.currentPrintJob,
                                onUpdateJob = { job -> vm.currentPrintJob = job },
                                onProceedToPreview = { vm.destination = KioskDestination.PRINT_PREVIEW },
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.SCANNER -> {
                            ScannerScreen(
                                currentJob = vm.currentPrintJob,
                                onUpdateJob = { job -> vm.currentPrintJob = job },
                                onProceedToPreview = { vm.destination = KioskDestination.PRINT_PREVIEW },
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.PRINT_PREVIEW -> {
                            PrintPreviewScreen(
                                currentJob = vm.currentPrintJob,
                                onProceedToPayment = { vm.destination = KioskDestination.PAYMENT },
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.PAYMENT -> {
                            PaymentScreen(
                                currentJob = vm.currentPrintJob,
                                onPaymentSuccess = { tx ->
                                    vm.lastTransaction = tx
                                    vm.destination = KioskDestination.PRINTING_PROGRESS
                                },
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.PRINTING_PROGRESS -> {
                            PrintingProgressScreen(
                                currentJob = vm.currentPrintJob,
                                transaction = vm.lastTransaction,
                                onDoneHome = { vm.destination = KioskDestination.HOME },
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.MEDICATION_REMINDER -> {
                            MedicationReminderScreen(
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.SOS_EMERGENCY -> {
                            SosEmergencyScreen(
                                locationInfo = vm.locationInfo,
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.LOCATION_SHARING -> {
                            LocationSharingScreen(
                                locationInfo = vm.locationInfo,
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.AI_CHAT -> {
                            AiChatScreen(
                                geminiService = geminiService,
                                onSpeak = ::speakText
                            )
                        }

                        KioskDestination.DEVICE_DOWNLOAD_QR -> {
                            DeviceDownloadQrScreen(
                                onSpeak = ::speakText
                            )
                        }
                    }

                    if (showBiometric) {
                        BiometricDialog(
                            onDismiss = { showBiometric = false },
                            onSuccess = { name ->
                                vm.isLoggedIn = true
                                vm.userName = name
                                showBiometric = false
                            },
                            onSpeak = ::speakText
                        )
                    }
                }
            }
        }
    }
}
