package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DocumentSource
import com.example.data.model.PrintJob
import com.example.data.model.ScanFilter
import com.example.services.DocumentEngine
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun PrintPreviewScreen(
    currentJob: PrintJob,
    onProceedToPayment: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    var currentPage by remember { mutableIntStateOf(1) }
    val totalPages = currentJob.totalPages

    // Render bitmap based on source and page
    val previewBitmap: Bitmap = remember(currentJob, currentPage) {
        val base = DocumentEngine.createSampleDocumentBitmap(
            title = currentJob.title,
            subtitle = "KERAJAAN MALAYSIA • DOKUMEN RASMI",
            isIdCard = currentJob.source == DocumentSource.SAMPLE_IC_CARD,
            pageNumber = currentPage
        )
        val filter = if (currentJob.isColor) currentJob.scanFilter else ScanFilter.PHOTOSTAT_BW
        DocumentEngine.applyScanFilter(base, filter)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("print_preview_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Top Pagination & Controls
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Halaman $currentPage daripada $totalPages",
                        fontSize = (15 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = {
                                if (currentPage > 1) {
                                    currentPage--
                                    onSpeak("Halaman $currentPage")
                                }
                            },
                            enabled = currentPage > 1,
                            modifier = Modifier.testTag("preview_prev_page")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Sebelumnya")
                        }

                        IconButton(
                            onClick = {
                                if (currentPage < totalPages) {
                                    currentPage++
                                    onSpeak("Halaman $currentPage")
                                }
                            },
                            enabled = currentPage < totalPages,
                            modifier = Modifier.testTag("preview_next_page")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Seterusnya")
                        }
                    }
                }
            }
        }

        // Real Document Preview Canvas Box
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else Color(0xFFE2E8F0)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("preview_document_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = previewBitmap.asImageBitmap(),
                        contentDescription = "Pratonton Dokumen Cetakan",
                        modifier = Modifier
                            .fillMaxWidth(0.92f)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        // Print Job Summary Details
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
                    Text(
                        text = "RINGKASAN PESANAN CETAKAN",
                        fontSize = (13 * textScale).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SummaryRow(label = "Dokumen", value = currentJob.title, isBigText = isBigText)
                    SummaryRow(label = "Saiz Kertas", value = currentJob.paperSize.displayName, isBigText = isBigText)
                    SummaryRow(label = "Mod Warna", value = if (currentJob.isColor) "Berwarna Penuh" else "Hitam & Putih", isBigText = isBigText)
                    SummaryRow(label = "Bilangan Salinan", value = "${currentJob.copies} Salinan", isBigText = isBigText)
                    SummaryRow(label = "Jumlah Helaian", value = "${currentJob.copies * currentJob.totalPages} Helaian", isBigText = isBigText)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Jumlah Yuran:",
                            fontSize = (16 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "RM ${String.format("%.2f", currentJob.totalPrice)}",
                            fontSize = (26 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF059669)
                        )
                    }
                }
            }
        }

        // Proceed to Payment Action Button
        item {
            Button(
                onClick = {
                    onSpeak("Sila sahkan bayaran nirtunai RM ${String.format("%.2f", currentJob.totalPrice)} melalui DuitNow.")
                    onProceedToPayment()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFED008C) // DuitNow Pink
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .testTag("btn_proceed_to_payment")
            ) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "Bayar Nirtunai",
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Bayar Nirtunai DuitNow (RM ${String.format("%.2f", currentJob.totalPrice)})",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = (16 * textScale).sp,
                    color = Color.White
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBigText: Boolean) {
    val textScale = if (isBigText) 1.25f else 1.0f
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = (13 * textScale).sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = (13 * textScale).sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
