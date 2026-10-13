package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MedicationReminder
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun MedicationReminderScreen(
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    // Sample default list for senior citizens
    val reminders = remember {
        mutableStateListOf(
            MedicationReminder(
                name = "Amlodipine (Ubat Tekanan Darah)",
                dosage = "1 Biji (5mg)",
                timeString = "08:00 Pagi",
                instruction = "Selepas Sarapan Pagi"
            ),
            MedicationReminder(
                name = "Metformin (Ubat Gula/Diabetes)",
                dosage = "1 Biji (500mg)",
                timeString = "01:00 Tengah Hari",
                instruction = "Bersama Makanan"
            ),
            MedicationReminder(
                name = "Atorvastatin (Ubat Kolesterol)",
                dosage = "1 Biji (20mg)",
                timeString = "09:00 Malam",
                instruction = "Sebelum Tidur"
            )
        )
    }

    var showAddDialog by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf("") }
    var newDosage by remember { mutableStateOf("1 Biji") }
    var newTime by remember { mutableStateOf("08:00 Pagi") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("medication_reminder_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner Header
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else Color(0xFF10B981).copy(alpha = 0.15f)
                ),
                border = BorderStroke(1.dp, Color(0xFF10B981)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalPharmacy,
                        contentDescription = "Ubat",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Peringatan Ubat Harian Pintar",
                            fontSize = (18 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isHighContrast) Color.White else Color(0xFF065F46)
                        )
                        Text(
                            text = "Penggera audio automatik untuk memastikan warga emas sentiasa mengambil dos ubat tepat pada masanya.",
                            fontSize = (13 * textScale).sp,
                            color = if (isHighContrast) Color.LightGray else Color(0xFF047857)
                        )
                    }
                }
            }
        }

        // Add Reminder Action
        item {
            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_add_medication")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah")
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Tambah Jadual Ubat Baharu",
                    fontWeight = FontWeight.Bold,
                    fontSize = (14 * textScale).sp
                )
            }
        }

        // Add Form Card (if expanded)
        if (showAddDialog) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Daftar Ubat Baharu",
                            fontSize = (16 * textScale).sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = newName,
                            onValueChange = { newName = it },
                            label = { Text("Nama Ubat (cth: Panadol)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = newDosage,
                                onValueChange = { newDosage = it },
                                label = { Text("Dos") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = newTime,
                                onValueChange = { newTime = it },
                                label = { Text("Waktu") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedButton(
                                onClick = { showAddDialog = false },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Batal")
                            }
                            Button(
                                onClick = {
                                    if (newName.isNotBlank()) {
                                        reminders.add(
                                            MedicationReminder(
                                                name = newName,
                                                dosage = newDosage,
                                                timeString = newTime
                                            )
                                        )
                                        onSpeak("Jadual ubat $newName pada jam $newTime berjaya disimpan.")
                                        newName = ""
                                        showAddDialog = false
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Simpan")
                            }
                        }
                    }
                }
            }
        }

        // List of Active Reminders
        items(reminders) { med ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.2f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Masa",
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = med.timeString,
                                fontSize = (16 * textScale).sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF059669)
                            )
                        }

                        Switch(
                            checked = med.isEnabled,
                            onCheckedChange = { isChecked ->
                                val index = reminders.indexOf(med)
                                if (index != -1) {
                                    reminders[index] = med.copy(isEnabled = isChecked)
                                    onSpeak(if (isChecked) "Peringatan ${med.name} diaktifkan." else "Peringatan ${med.name} dipadamkan.")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = med.name,
                        fontSize = (15 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Dos: ${med.dosage} • ${med.instruction}",
                        fontSize = (13 * textScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Test Voice Announcement
                        OutlinedButton(
                            onClick = {
                                onSpeak("Perhatian warga emas: Waktunya untuk mengambil ${med.name}, ${med.dosage}, ${med.instruction}.")
                            },
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Dengar",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Dengar Suara Penggera", fontSize = (11 * textScale).sp)
                        }

                        // Mark as Taken
                        Button(
                            onClick = {
                                onSpeak("Tahniah! Anda telah menanda ubat ${med.name} sebagai sudah diambil.")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selesai",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Tanda Diambil", fontSize = (11 * textScale).sp)
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
