package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KioskLocationInfo
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun SosEmergencyScreen(
    locationInfo: KioskLocationInfo,
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    var isAlarmActive by remember { mutableStateOf(false) }

    // Pulsing siren animation
    val infiniteTransition = rememberInfiniteTransition(label = "sos_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("sos_emergency_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Warning Banner
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isAlarmActive) Color(0xFFEF4444) else Color(0xFFFEF2F2)
                ),
                border = BorderStroke(2.dp, Color(0xFFEF4444)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isAlarmActive) "SIREN KECEMASAN AKTIF!" else "SISTEM BANTUAN KECEMASAN SATU-SENTUHAN",
                        fontSize = (16 * textScale).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isAlarmActive) Color.White else Color(0xFF991B1B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Tekan butang merah di bawah untuk menghubungi bantuan perubatan, keselamatan, atau staf kiosk dengan serta-merta.",
                        fontSize = (13 * textScale).sp,
                        color = if (isAlarmActive) Color.White.copy(alpha = 0.9f) else Color(0xFF7F1D1D),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // GIANT ONE-TOUCH SOS BUTTON (Accessible for Elderly & OKU)
        item {
            Surface(
                shape = CircleShape,
                color = Color(0xFFEF4444).copy(alpha = if (isAlarmActive) 0.35f else 0.15f),
                modifier = Modifier
                    .size(190.dp)
                    .padding(8.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            isAlarmActive = true
                            onSpeak("AMARAN KECEMASAN! Bantuan kecemasan kiosk diaktifkan di ${locationInfo.venueName}. Talian bantuan sedang dihubungi!")
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFEF4444)
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .size(150.dp)
                            .testTag("btn_giant_sos_trigger")
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "SOS",
                                tint = Color.White,
                                modifier = Modifier.size(52.dp)
                            )
                            Text(
                                text = "SOS",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Quick Direct Call Hotlines
        item {
            Text(
                text = "TALIAN KECEMASAN TERUS",
                fontSize = (13 * textScale).sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Dial 999
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:999"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("btn_dial_999")
                ) {
                    Icon(Icons.Default.LocalHospital, contentDescription = "999")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Talian 999", fontWeight = FontWeight.Bold, fontSize = (14 * textScale).sp)
                }

                // Operator Kiosk line
                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${locationInfo.helpline}"))
                        context.startActivity(intent)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .testTag("btn_dial_operator")
                ) {
                    Icon(Icons.Default.Call, contentDescription = "Operator")
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Staf Kiosk", fontWeight = FontWeight.Bold, fontSize = (14 * textScale).sp)
                }
            }
        }

        // Send Emergency SMS Broadcast
        item {
            OutlinedButton(
                onClick = {
                    val smsText = "KECEMASAN KIOSK! Bantuan diperlukan di ${locationInfo.venueName}, ${locationInfo.address}. Koordinat: ${locationInfo.latitude}, ${locationInfo.longitude}"
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:999")
                        putExtra("sms_body", smsText)
                    }
                    context.startActivity(intent)
                    onSpeak("Mesej kecemasan dengan lokasi kiosk telah dihantar.")
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_send_sos_sms")
            ) {
                Icon(Icons.Default.Sms, contentDescription = "SMS")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hantar SMS Siaran Kecemasan dengan GPS",
                    fontWeight = FontWeight.Bold,
                    fontSize = (13 * textScale).sp
                )
            }
        }

        // Kiosk Location Info Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "MAKLUMAT LOKASI KIOSK INI:",
                        fontSize = (12 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${locationInfo.venueName} (${locationInfo.kioskId})",
                        fontSize = (14 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = locationInfo.address,
                        fontSize = (12 * textScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        if (isAlarmActive) {
            item {
                OutlinedButton(
                    onClick = {
                        isAlarmActive = false
                        onSpeak("Siren kecemasan telah dimatikan.")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Matikan Siren Kecemasan")
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
