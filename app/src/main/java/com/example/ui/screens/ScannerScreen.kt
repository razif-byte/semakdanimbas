package com.example.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
fun ScannerScreen(
    currentJob: PrintJob,
    onUpdateJob: (PrintJob) -> Unit,
    onProceedToPreview: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    var selectedResolution by remember { mutableStateOf("1200 DPI (Ultra HD)") }
    var currentFilter by remember { mutableStateOf(ScanFilter.PHOTOSTAT_BW) }
    var isScanningActive by remember { mutableStateOf(false) }
    var scanProgress by remember { mutableStateOf(100) }
    var isFrontSide by remember { mutableStateOf(true) }

    // Generates raw sample bitmap (e.g. IC card)
    val baseBitmap = remember(currentJob.source, isFrontSide) {
        DocumentEngine.createSampleDocumentBitmap(
            title = "DOKUMEN IMBASAN KIOSK",
            subtitle = if (isFrontSide) "BAHAGIAN HADAPAN" else "BAHAGIAN BELAKANG",
            isIdCard = true
        )
    }

    // Filtered preview bitmap
    val processedBitmap: Bitmap = remember(baseBitmap, currentFilter) {
        DocumentEngine.applyScanFilter(baseBitmap, currentFilter)
    }

    // Laser scan animation
    val infiniteTransition = rememberInfiniteTransition(label = "scanner_laser")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser_pos"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("scanner_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Scanner Bed Status
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HighQuality,
                                contentDescription = "Resolusi Tinggi",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Pengimbas Kaca Optik Resolusi Tinggi",
                                fontSize = (15 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = selectedResolution,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Live Scanner Bed Preview Box
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black
                ),
                border = BorderStroke(2.dp, Color(0xFF38BDF8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .testTag("scanner_bed_preview")
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    // Document Image on scanner bed
                    Image(
                        bitmap = processedBitmap.asImageBitmap(),
                        contentDescription = "Hasil Imbasan Dokumen",
                        modifier = Modifier
                            .padding(20.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    )

                    // Laser beam sweeping over bed
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .align(Alignment.TopCenter)
                            .padding(top = (270 * laserY).dp)
                            .background(Color(0xFF38BDF8))
                    )

                    // Scanner Guide Grid overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                            .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.25f), RoundedCornerShape(6.dp))
                    )

                    // Side Indicator
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.Black.copy(alpha = 0.75f),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = if (isFrontSide) "Muka Depan (Depan)" else "Muka Belakang",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Scanner Quick Tools (Flip Side, Re-Scan)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        isFrontSide = !isFrontSide
                        onSpeak(if (isFrontSide) "Dibalikkan ke muka depan." else "Dibalikkan ke muka belakang kad.")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_flip_side"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Flip, contentDescription = "Balikkan")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Balikkan Muka", fontSize = (13 * textScale).sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        isScanningActive = true
                        onSpeak("Memulakan imbasan optik beresolusi tinggi 1200 DPI...")
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("btn_trigger_scan"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Imbas Semula")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Imbas Sekarang", fontSize = (13 * textScale).sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Filter Selection for Photostat & Document Enhancements
        item {
            Text(
                text = "MOD PENAPIS FOTOSTAT & IMBASAN",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = (14 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ScanFilter.values().forEach { filter ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (currentFilter == filter) {
                                if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            } else {
                                if (isHighContrast) Color(0xFF121212) else MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = BorderStroke(
                            width = if (currentFilter == filter) 2.dp else 1.dp,
                            color = if (currentFilter == filter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                currentFilter = filter
                                onSpeak("Mod penapis ${filter.displayName} dipilih.")
                            }
                            .testTag("filter_${filter.name}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoFixHigh,
                                    contentDescription = filter.displayName,
                                    tint = if (currentFilter == filter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = filter.displayName,
                                    fontSize = (14 * textScale).sp,
                                    fontWeight = if (currentFilter == filter) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (currentFilter == filter) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Aktif",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Resolution Option Chips
        item {
            Text(
                text = "RESOLUSI IMBASAN OPTIK",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = (14 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("300 DPI (Standard)", "600 DPI (Jelas)", "1200 DPI (Ultra HD)").forEach { dpi ->
                    FilterChip(
                        selected = selectedResolution == dpi,
                        onClick = {
                            selectedResolution = dpi
                            onSpeak("Resolusi $dpi dipilih.")
                        },
                        label = { Text(dpi, fontSize = (12 * textScale).sp) },
                        modifier = Modifier.testTag("chip_dpi_$dpi")
                    )
                }
            }
        }

        // Proceed to Print Preview Button
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Status Imbasan:",
                            fontSize = (12 * textScale).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Sedia untuk Dicetak",
                            fontSize = (16 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669)
                        )
                    }

                    Button(
                        onClick = {
                            val isColor = currentFilter == ScanFilter.ORIGINAL || currentFilter == ScanFilter.ENHANCED_SHARP
                            val base = if (isColor) 0.80 else 0.20
                            val total = base * currentJob.copies * 1 * currentJob.paperSize.basePriceMultiplier
                            onUpdateJob(
                                currentJob.copy(
                                    title = "Imbasan Fotostat Resolusi Tinggi",
                                    source = DocumentSource.SAMPLE_IC_CARD,
                                    scanFilter = currentFilter,
                                    isColor = isColor,
                                    pricePerUnit = base,
                                    totalPrice = total
                                )
                            )
                            onSpeak("Imbasan selesai. Membuka pratonton cetakan fotostat.")
                            onProceedToPreview()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("btn_scanner_proceed_preview")
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = "Pratonton")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pratonton & Cetak",
                            fontWeight = FontWeight.Bold,
                            fontSize = (14 * textScale).sp
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
