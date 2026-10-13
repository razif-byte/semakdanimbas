package com.example.ui.screens

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.InstallMobile
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCode
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.services.DuitNowQrEngine
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun DeviceDownloadQrScreen(
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    // Detect hardware & Android specs
    val deviceModel = "${Build.MANUFACTURER.replaceFirstChar { it.uppercase() }} ${Build.MODEL}"
    val osVersion = "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    val abi = Build.SUPPORTED_ABIS.firstOrNull() ?: "arm64-v8a"
    val deviceInfoString = "$deviceModel • $abi"

    val downloadUrl = "https://nasadef.com.my/kiosk-app-download?device=${Uri.encode(deviceModel)}&abi=$abi"

    val qrBitmap: Bitmap = remember(downloadUrl) {
        DuitNowQrEngine.generateAppDownloadQr(downloadUrl, deviceInfoString)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("device_download_qr_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Banner Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else Color(0xFF059669).copy(alpha = 0.12f)
                ),
                border = BorderStroke(1.dp, Color(0xFF059669)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.InstallMobile,
                        contentDescription = "Pasang Aplikasi",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Auto Download & Pasang Aplikasi",
                            fontSize = (18 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isHighContrast) Color.White else Color(0xFF065F46)
                        )
                        Text(
                            text = "Kod QR pintar ini disesuaikan khusus mengikut model dan versi peranti Android anda.",
                            fontSize = (13 * textScale).sp,
                            color = if (isHighContrast) Color.LightGray else Color(0xFF047857)
                        )
                    }
                }
            }
        }

        // Detected Device Specs
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
                        text = "MAKLUMAT PERANTI ANDA YANG DIKESAN:",
                        fontSize = (12 * textScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    SummaryRow(label = "Model Peranti", value = deviceModel, isBigText = isBigText)
                    SummaryRow(label = "Sistem Operasi", value = osVersion, isBigText = isBigText)
                    SummaryRow(label = "Seni Bina Pemproses", value = abi, isBigText = isBigText)
                    SummaryRow(label = "Pakej APK Optimum", value = "Kiosk-v1.0-$abi.apk", isBigText = isBigText)
                }
            }
        }

        // Custom QR Code Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("device_download_qr_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "Kod QR Auto Download",
                        modifier = Modifier
                            .size(270.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Halakan kamera telefon anda ke kod QR ini untuk memuat turun dan memasang aplikasi secara automatik.",
                        fontSize = (13 * textScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Direct Download Action
        item {
            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(downloadUrl))
                    context.startActivity(intent)
                    onSpeak("Membuka pautan muat turun aplikasi terus pada pelayar telefon anda.")
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_direct_download_apk")
            ) {
                Icon(Icons.Default.Download, contentDescription = "Muat Turun")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Muat Turun Terus Pada Peranti Ini (APK)",
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
