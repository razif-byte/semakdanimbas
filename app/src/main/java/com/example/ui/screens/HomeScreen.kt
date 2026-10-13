package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

enum class KioskDestination {
    HOME,
    DOCUMENT_PRINT,
    SCANNER,
    PRINT_PREVIEW,
    PAYMENT,
    PRINTING_PROGRESS,
    MEDICATION_REMINDER,
    SOS_EMERGENCY,
    LOCATION_SHARING,
    AI_CHAT,
    DEVICE_DOWNLOAD_QR
}

@Composable
fun HomeScreen(
    isLoggedIn: Boolean,
    userName: String,
    onNavigate: (KioskDestination) -> Unit,
    onOpenBiometric: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("kiosk_home_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Kiosk Header Banner
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(20.dp),
                border = if (isHighContrast) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSpeak("Selamat datang ke Kiosk Percetakan dan Fotostat Layan Diri Nasadef. Sedia membantu anda.")
                    }
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(Color(0xFF10B981), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "KIOSK AKTIF & BEROPERASI",
                                fontSize = (12 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        // Biometric Quick Button
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isLoggedIn) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { onOpenBiometric() }
                                .testTag("home_biometric_badge")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isLoggedIn) Icons.Default.CheckCircle else Icons.Default.Fingerprint,
                                    contentDescription = "Log Masuk Biometrik",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isLoggedIn) userName else "Log Masuk Cap Jari",
                                    fontSize = (12 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Kiosk Cetak & Fotostat Pintar",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = (22 * textScale).sp
                        ),
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isHighContrast) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Sistem layan diri nirtunai DuitNow dengan pengimbas beresolusi tinggi, sokongan warga emas, OKU & panduan suara.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (14 * textScale).sp
                        ),
                        color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Hardware Readiness status indicators
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusChip(label = "Kertas: 500 Helaian", isBigText = isBigText)
                        StatusChip(label = "Dakwat: 98% Penuh", isBigText = isBigText)
                        StatusChip(label = "Pengimbas: 1200 DPI", isBigText = isBigText)
                    }
                }
            }
        }

        // Section: Servis Percetakan & Fotostat
        item {
            Text(
                text = "SERVIS PERCETAKAN UTAMA",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = (13 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            KioskServiceButton(
                title = "Cetak Dokumen & PDF",
                subtitle = "Pilih fail PDF, borang rasmi kerajaan, resume & sijil",
                icon = Icons.Default.Print,
                accentColor = Color(0xFF0284C7),
                testTag = "btn_cetak_dokumen",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka menu Cetak Dokumen dan PDF.")
                    onNavigate(KioskDestination.DOCUMENT_PRINT)
                }
            )
        }

        item {
            KioskServiceButton(
                title = "Fotostat & Pengimbas Resolusi Tinggi",
                subtitle = "Imbas kad pengenalan (IC), dokumen berkontras tinggi & pratonton",
                icon = Icons.Default.DocumentScanner,
                accentColor = Color(0xFF0D9488),
                testTag = "btn_fotostat_pengimbas",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka Pengimbas Dokumen Resolusi Tinggi.")
                    onNavigate(KioskDestination.SCANNER)
                }
            )
        }

        item {
            KioskServiceButton(
                title = "Pembayaran Nirtunai DuitNow",
                subtitle = "CIMB (7016657934) & TNG DuitNow (170997196374) masa nyata",
                icon = Icons.Default.Payment,
                accentColor = Color(0xFFED008C),
                testTag = "btn_pembayaran_nirtunai",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka sistem pembayaran nirtunai e-wallet DuitNow.")
                    onNavigate(KioskDestination.PAYMENT)
                }
            )
        }

        // Section: Aksesibiliti Warga Emas & Kesihatan
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "KEMUDAHAN WARGA EMAS & KELUARGA",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = (13 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        item {
            KioskServiceButton(
                title = "Peringatan Ubat Harian",
                subtitle = "Jadual makan ubat automatik dengan penggera suara",
                icon = Icons.Default.LocalPharmacy,
                accentColor = Color(0xFF10B981),
                testTag = "btn_peringatan_ubat",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka jadual Peringatan Ubat Harian.")
                    onNavigate(KioskDestination.MEDICATION_REMINDER)
                }
            )
        }

        item {
            KioskServiceButton(
                title = "Perkongsian Lokasi Keluarga Masa Nyata",
                subtitle = "Kongsi lokasi kiosk ini terus ke WhatsApp atau SMS keluarga",
                icon = Icons.Default.LocationOn,
                accentColor = Color(0xFF6366F1),
                testTag = "btn_perkongsian_lokasi",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka ciri Perkongsian Lokasi Keluarga.")
                    onNavigate(KioskDestination.LOCATION_SHARING)
                }
            )
        }

        item {
            KioskServiceButton(
                title = "Bantuan Kecemasan SOS 1-Sentuhan",
                subtitle = "Talian kecemasan 999 & bantuan operator kiosk terus",
                icon = Icons.Default.Warning,
                accentColor = Color(0xFFEF4444),
                testTag = "btn_bantuan_sos",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka sistem Bantuan Kecemasan SOS satu sentuhan.")
                    onNavigate(KioskDestination.SOS_EMERGENCY)
                }
            )
        }

        // Section: Bantuan AI & Aplikasi Mudah Alih
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "BANTUAN AI & APLIKASI PERANTI",
                style = MaterialTheme.typography.labelLarge.copy(fontSize = (13 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.tertiary
            )
        }

        item {
            KioskServiceButton(
                title = "Sokongan Sembang AI Langsung",
                subtitle = "Pembantu pintar Gemini untuk bantuan soalan & panduan teknikal",
                icon = Icons.Default.Chat,
                accentColor = Color(0xFF8B5CF6),
                testTag = "btn_sembang_ai",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka Sokongan Sembang AI secara langsung.")
                    onNavigate(KioskDestination.AI_CHAT)
                }
            )
        }

        item {
            KioskServiceButton(
                title = "Auto Download Aplikasi Mudah Alih",
                subtitle = "Kod QR disesuaikan peranti untuk pasang app ini di telefon anda",
                icon = Icons.Default.QrCode2,
                accentColor = Color(0xFF059669),
                testTag = "btn_download_app_qr",
                isBigText = isBigText,
                onClick = {
                    onSpeak("Membuka kod QR muat turun aplikasi peranti.")
                    onNavigate(KioskDestination.DEVICE_DOWNLOAD_QR)
                }
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun StatusChip(label: String, isBigText: Boolean) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        tonalElevation = 2.dp
    ) {
        Text(
            text = label,
            fontSize = if (isBigText) 12.sp else 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun KioskServiceButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    testTag: String,
    isBigText: Boolean,
    onClick: () -> Unit
) {
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isHighContrast) 2.dp else 1.dp,
            color = if (isHighContrast) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = accentColor.copy(alpha = 0.15f),
                border = BorderStroke(1.5.dp, accentColor),
                modifier = Modifier.size(54.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = accentColor,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = (17 * textScale).sp
                    ),
                    fontWeight = FontWeight.Bold,
                    color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = (13 * textScale).sp
                    ),
                    color = if (isHighContrast) Color(0xFFE2E8F0) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
