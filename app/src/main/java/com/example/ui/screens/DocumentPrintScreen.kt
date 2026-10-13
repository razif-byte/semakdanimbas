package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.example.data.model.DocumentSource
import com.example.data.model.PaperSize
import com.example.data.model.PrintJob
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun DocumentPrintScreen(
    currentJob: PrintJob,
    onUpdateJob: (PrintJob) -> Unit,
    onProceedToPreview: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("document_print_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "1. PILIH DOKUMEN UNTUK DICETAK",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = (15 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            DocumentSourceCard(
                title = "Borang Permohonan Rasmi Kerajaan",
                desc = "Fail PDF Standard (2 Helaian) • Format A4 Rasmi",
                icon = Icons.Default.Description,
                isSelected = currentJob.source == DocumentSource.SAMPLE_GOVERNMENT_FORM,
                testTag = "source_gov_form",
                isBigText = isBigText,
                onClick = {
                    val base = if (currentJob.isColor) 0.80 else 0.20
                    val total = base * currentJob.copies * 2 * currentJob.paperSize.basePriceMultiplier
                    onUpdateJob(
                        currentJob.copy(
                            title = "Borang Permohonan Rasmi Kerajaan",
                            source = DocumentSource.SAMPLE_GOVERNMENT_FORM,
                            totalPages = 2,
                            pricePerUnit = base,
                            totalPrice = total
                        )
                    )
                    onSpeak("Dipilih Borang Permohonan Rasmi Kerajaan, dua helaian.")
                }
            )
        }

        item {
            DocumentSourceCard(
                title = "Salinan Kad Pengenalan (MyKad / IC)",
                desc = "Format Kad Rasmi Malaysia Depan & Belakang",
                icon = Icons.Default.Badge,
                isSelected = currentJob.source == DocumentSource.SAMPLE_IC_CARD,
                testTag = "source_ic_card",
                isBigText = isBigText,
                onClick = {
                    val base = if (currentJob.isColor) 0.80 else 0.20
                    val total = base * currentJob.copies * 1 * currentJob.paperSize.basePriceMultiplier
                    onUpdateJob(
                        currentJob.copy(
                            title = "Salinan Kad Pengenalan (MyKad)",
                            source = DocumentSource.SAMPLE_IC_CARD,
                            totalPages = 1,
                            pricePerUnit = base,
                            totalPrice = total
                        )
                    )
                    onSpeak("Dipilih Salinan Kad Pengenalan MyKad.")
                }
            )
        }

        item {
            DocumentSourceCard(
                title = "Sijil Perakuan & Dokumen Penting",
                desc = "Dokumen Sijil Kelahiran / Akademik Resolusi Tinggi",
                icon = Icons.Default.HistoryEdu,
                isSelected = currentJob.source == DocumentSource.SAMPLE_CERTIFICATE,
                testTag = "source_certificate",
                isBigText = isBigText,
                onClick = {
                    val base = if (currentJob.isColor) 0.80 else 0.20
                    val total = base * currentJob.copies * 1 * currentJob.paperSize.basePriceMultiplier
                    onUpdateJob(
                        currentJob.copy(
                            title = "Sijil Perakuan Penting",
                            source = DocumentSource.SAMPLE_CERTIFICATE,
                            totalPages = 1,
                            pricePerUnit = base,
                            totalPrice = total
                        )
                    )
                    onSpeak("Dipilih Sijil Perakuan Penting.")
                }
            )
        }

        item {
            DocumentSourceCard(
                title = "Muat Naik Fail PDF / Gambar Sendiri",
                desc = "Pilih dokumen dari storan telefon pintar anda",
                icon = Icons.Default.FileUpload,
                isSelected = currentJob.source == DocumentSource.DEVICE_FILE,
                testTag = "source_device_file",
                isBigText = isBigText,
                onClick = {
                    val base = if (currentJob.isColor) 0.80 else 0.20
                    val total = base * currentJob.copies * 1 * currentJob.paperSize.basePriceMultiplier
                    onUpdateJob(
                        currentJob.copy(
                            title = "Fail PDF / Gambar Pengguna",
                            source = DocumentSource.DEVICE_FILE,
                            totalPages = 1,
                            pricePerUnit = base,
                            totalPrice = total
                        )
                    )
                    onSpeak("Mod muat naik fail PDF peranti dipilih.")
                }
            )
        }

        // Section: Tetapan Cetakan
        item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "2. TETAPAN & PILIHAN CETAKAN",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = (15 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Color Mode Toggle Card
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
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "Mod Warna",
                                tint = if (currentJob.isColor) Color(0xFFE11D48) else Color(0xFF334155),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (currentJob.isColor) "Cetakan Berwarna Penuh" else "Cetakan Hitam & Putih",
                                    fontSize = (16 * textScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (currentJob.isColor) "RM 0.80 sehelai (Warna)" else "RM 0.20 sehelai (Hitam & Putih)",
                                    fontSize = (13 * textScale).sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Switch(
                            checked = currentJob.isColor,
                            onCheckedChange = { isColor ->
                                val base = if (isColor) 0.80 else 0.20
                                val total = base * currentJob.copies * currentJob.totalPages * currentJob.paperSize.basePriceMultiplier
                                onUpdateJob(currentJob.copy(isColor = isColor, pricePerUnit = base, totalPrice = total))
                                onSpeak(if (isColor) "Mod cetak warna penuh diaktifkan, 80 sen sehelai." else "Mod cetak hitam putih diaktifkan, 20 sen sehelai.")
                            },
                            modifier = Modifier.testTag("toggle_color_switch")
                        )
                    }
                }
            }
        }

        // Copies Selector
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
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Bilangan Salinan (Copies)",
                            fontSize = (15 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${currentJob.copies} Salinan x ${currentJob.totalPages} Halaman",
                            fontSize = (13 * textScale).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = {
                                if (currentJob.copies > 1) {
                                    val newCopies = currentJob.copies - 1
                                    val total = currentJob.pricePerUnit * newCopies * currentJob.totalPages * currentJob.paperSize.basePriceMultiplier
                                    onUpdateJob(currentJob.copy(copies = newCopies, totalPrice = total))
                                    onSpeak("$newCopies salinan.")
                                }
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("btn_minus_copies"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Kurang")
                        }

                        Text(
                            text = "${currentJob.copies}",
                            fontSize = (18 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )

                        Button(
                            onClick = {
                                val newCopies = currentJob.copies + 1
                                val total = currentJob.pricePerUnit * newCopies * currentJob.totalPages * currentJob.paperSize.basePriceMultiplier
                                onUpdateJob(currentJob.copy(copies = newCopies, totalPrice = total))
                                onSpeak("$newCopies salinan.")
                            },
                            modifier = Modifier
                                .size(44.dp)
                                .testTag("btn_plus_copies"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Tambah")
                        }
                    }
                }
            }
        }

        // Paper Size Selector
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
                        text = "Saiz Kertas",
                        fontSize = (15 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PaperSize.values().forEach { size ->
                            FilterChip(
                                selected = currentJob.paperSize == size,
                                onClick = {
                                    val total = currentJob.pricePerUnit * currentJob.copies * currentJob.totalPages * size.basePriceMultiplier
                                    onUpdateJob(currentJob.copy(paperSize = size, totalPrice = total))
                                    onSpeak("Saiz ${size.displayName} dipilih.")
                                },
                                label = { Text(size.displayName, fontSize = (12 * textScale).sp) },
                                modifier = Modifier.testTag("chip_size_${size.name}")
                            )
                        }
                    }
                }
            }
        }

        // Summary Price & Preview Button
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
                            text = "Jumlah Yuran Cetakan:",
                            fontSize = (13 * textScale).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "RM ${String.format("%.2f", currentJob.totalPrice)}",
                            fontSize = (26 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF059669)
                        )
                    }

                    Button(
                        onClick = {
                            onSpeak("Membuka pratonton cetakan untuk dokumen ${currentJob.title}.")
                            onProceedToPreview()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(52.dp)
                            .testTag("btn_proceed_to_preview")
                    ) {
                        Icon(Icons.Default.Visibility, contentDescription = "Pratonton")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pratonton Cetakan",
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

@Composable
fun DocumentSourceCard(
    title: String,
    desc: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    isBigText: Boolean,
    onClick: () -> Unit
) {
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            } else {
                if (isHighContrast) Color(0xFF121212) else MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = (15 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = desc,
                    fontSize = (12 * textScale).sp,
                    color = if (isHighContrast) Color(0xFFCBD5E1) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Dipilih",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
