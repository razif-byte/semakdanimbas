package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode
import kotlinx.coroutines.delay

@Composable
fun BiometricDialog(
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Fingerprint, 1: Face
    var isVerifying by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "sensor_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    if (isVerifying) {
        LaunchedEffect(Unit) {
            delay(1200)
            isVerifying = false
            isSuccess = true
            val name = "Ahmad Razif (Warga Emas)"
            onSpeak("Pengesahan biometrik berjaya! Selamat datang, Encik Ahmad Razif.")
            delay(800)
            onSuccess(name)
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("biometric_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Log Masuk Biometrik Mudah",
                    fontSize = (18 * textScale).sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Sesuai untuk kemudahan warga emas dan semua pengguna.",
                    fontSize = (13 * textScale).sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Tabs: Cap Jari vs Imbasan Wajah
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            onSpeak("Pilihan imbasan cap jari dipilih.")
                        },
                        text = { Text("Cap Jari", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            onSpeak("Pilihan imbasan wajah dipilih.")
                        },
                        text = { Text("Imbasan Wajah", fontWeight = FontWeight.Bold) }
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Interactive Biometric Sensor Touch Target
                Surface(
                    shape = CircleShape,
                    color = if (isSuccess) Color(0xFF10B981).copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary.copy(alpha = if (isVerifying) pulseAlpha else 0.15f),
                    border = BorderStroke(3.dp, if (isSuccess) Color(0xFF10B981) else MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(110.dp)
                        .clickable {
                            if (!isVerifying && !isSuccess) {
                                isVerifying = true
                                onSpeak(if (selectedTab == 0) "Membaca cap jari pada pengimbas..." else "Mengimbas wajah melalui kamera pengimbas...")
                            }
                        }
                        .testTag("btn_biometric_sensor_target")
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when {
                                isSuccess -> Icons.Default.CheckCircle
                                selectedTab == 0 -> Icons.Default.Fingerprint
                                else -> Icons.Default.Face
                            },
                            contentDescription = "Sensor",
                            tint = if (isSuccess) Color(0xFF10B981) else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(56.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = when {
                        isSuccess -> "Disahkan! Selamat Datang."
                        isVerifying -> "Sedang Menentusahkan..."
                        selectedTab == 0 -> "Sentuh Pengimbas Cap Jari"
                        else -> "Pandang ke Kamera Imbasan Wajah"
                    },
                    fontSize = (15 * textScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSuccess) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                // One-Tap Elderly Easy Bypass (Crucial accessibility feature)
                Button(
                    onClick = {
                        isSuccess = true
                        val name = "Ahmad Razif (Warga Emas)"
                        onSpeak("Log masuk pantas 1-sentuhan warga emas berjaya.")
                        onSuccess(name)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_elderly_one_touch_bypass")
                ) {
                    Text(
                        text = "Log Masuk Mudah Warga Emas (1-Sentuhan)",
                        fontWeight = FontWeight.Bold,
                        fontSize = (13 * textScale).sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                ) {
                    Text("Batal")
                }
            }
        }
    }
}
