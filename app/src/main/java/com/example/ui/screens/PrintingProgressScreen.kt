package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PaymentTransaction
import com.example.data.model.PrintJob
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode
import kotlinx.coroutines.delay

@Composable
fun PrintingProgressScreen(
    currentJob: PrintJob,
    transaction: PaymentTransaction?,
    onDoneHome: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    var rawProgress by remember { mutableFloatStateOf(0.05f) }
    var isCompleted by remember { mutableStateOf(false) }
    var showReceipt by remember { mutableStateOf(false) }

    val animatedProgress by animateFloatAsState(targetValue = rawProgress, label = "print_progress")

    LaunchedEffect(Unit) {
        onSpeak("Mencetak dokumen ${currentJob.title}. Sila tunggu sebentar.")
        while (rawProgress < 1.0f) {
            delay(400)
            rawProgress = (rawProgress + 0.15f).coerceAtMost(1.0f)
        }
        delay(600)
        isCompleted = true
        onSpeak("Percetakan selesai sepenuhnya! Sila ambil dokumen fizikal anda di dulang pengumpulan.")
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("printing_progress_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Kiosk Printing Animation Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isCompleted) Color(0xFF10B981).copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Print,
                                contentDescription = "Status Cetak",
                                tint = if (isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isCompleted) "Percetakan Selesai!" else "Sedang Mencetak Dokumen...",
                        fontSize = (20 * textScale).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isCompleted) {
                            "Dokumen telah dikeluarkan ke dulang pengumpulan di bawah."
                        } else {
                            "Helaian sedang diproses dengan resolusi tinggi. Sila jangan tarik kertas."
                        },
                        fontSize = (13 * textScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Progress Bar
                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp),
                        color = if (isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${(animatedProgress * 100).toInt()}% Selesai",
                        fontSize = (13 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Collect Paper Alert Banner (Crucial for elderly/OKU)
        if (isCompleted) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF10B981).copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(2.dp, Color(0xFF10B981)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Ambil Kertas",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "SILA AMBIL KERTAS ANDA",
                                fontSize = (15 * textScale).sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF059669)
                            )
                            Text(
                                text = "Periksa dulang keluar di bahagian bawah kanan kiosk.",
                                fontSize = (12 * textScale).sp,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }

            // Digital Receipt Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "RESIT RASMI KIOSK CETAKAN",
                                fontSize = (13 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = "Resit",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        SummaryRow(label = "Dokumen", value = currentJob.title, isBigText = isBigText)
                        SummaryRow(label = "Jumlah Halaman", value = "${currentJob.copies * currentJob.totalPages} Helaian", isBigText = isBigText)
                        SummaryRow(label = "Kaedah Bayaran", value = transaction?.provider?.bankTitle ?: "DuitNow QR", isBigText = isBigText)
                        SummaryRow(label = "No Rujukan", value = transaction?.referenceNo ?: "REF-884920", isBigText = isBigText)
                        SummaryRow(label = "Jumlah Dibayar", value = "RM ${String.format("%.2f", currentJob.totalPrice)}", isBigText = isBigText)
                        SummaryRow(label = "Penyedia Kiosk", value = "NASADEF SDN BHD", isBigText = isBigText)
                    }
                }
            }

            // Finish & Home Action Buttons
            item {
                Button(
                    onClick = {
                        onSpeak("Terima kasih kerana menggunakan Kiosk Percetakan Nasadef. Semoga hari anda ceria.")
                        onDoneHome()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("btn_done_home")
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Utama")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Selesai & Kembali ke Menu Utama",
                        fontWeight = FontWeight.Bold,
                        fontSize = (15 * textScale).sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
