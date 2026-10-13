package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.KioskLocationInfo
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun LocationSharingScreen(
    locationInfo: KioskLocationInfo,
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    val mapsLink = "https://maps.google.com/?q=${locationInfo.latitude},${locationInfo.longitude}"
    val shareMessage = "Salam keluarga, saya kini berada di Kiosk Percetakan & Fotostat Layan Diri Nasadef (${locationInfo.venueName}). Alamat: ${locationInfo.address}. Pautan Peta: $mapsLink"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("location_sharing_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Banner Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else Color(0xFF6366F1).copy(alpha = 0.12f)
                ),
                border = BorderStroke(1.dp, Color(0xFF6366F1)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Lokasi",
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Perkongsian Lokasi Keluarga",
                            fontSize = (18 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isHighContrast) Color.White else Color(0xFF3730A3)
                        )
                        Text(
                            text = "Kongsi lokasi kehadiran anda di kiosk ini bersama ahli keluarga tersayang untuk ketenangan fikiran.",
                            fontSize = (13 * textScale).sp,
                            color = if (isHighContrast) Color.LightGray else Color(0xFF4338CA)
                        )
                    }
                }
            }
        }

        // Live Location Card
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
                        text = "LOKASI SEMASA KIOSK:",
                        fontSize = (12 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = locationInfo.venueName,
                        fontSize = (16 * textScale).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = locationInfo.address,
                        fontSize = (13 * textScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "GPS: ${locationInfo.latitude}° U, ${locationInfo.longitude}° T • ID: ${locationInfo.kioskId}",
                            fontSize = (12 * textScale).sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Quick Share Buttons
        item {
            Text(
                text = "HANTAR LOKASI KEPADA KELUARGA",
                fontSize = (13 * textScale).sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Share via WhatsApp / All Apps
        item {
            Button(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareMessage)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, "Kongsi Lokasi Kiosk kepada Keluarga")
                    context.startActivity(shareIntent)
                    onSpeak("Membuka aplikasi perkongsian lokasi untuk dihantar kepada keluarga.")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)), // WhatsApp Green
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("btn_share_whatsapp")
            ) {
                Icon(Icons.Default.Share, contentDescription = "Kongsi", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Kongsi ke WhatsApp / Aplikasi Keluarga",
                    fontWeight = FontWeight.Bold,
                    fontSize = (14 * textScale).sp,
                    color = Color.White
                )
            }
        }

        // Open in Google Maps
        item {
            OutlinedButton(
                onClick = {
                    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mapsLink))
                    context.startActivity(mapIntent)
                    onSpeak("Membuka Google Maps untuk melihat peta kiosk.")
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("btn_open_maps")
            ) {
                Icon(Icons.Default.Map, contentDescription = "Peta")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buka Peta Google Maps",
                    fontWeight = FontWeight.Bold,
                    fontSize = (14 * textScale).sp
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
