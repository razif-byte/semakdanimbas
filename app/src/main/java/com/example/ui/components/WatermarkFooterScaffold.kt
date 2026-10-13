package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode

@Composable
fun WatermarkFooterScaffold(
    title: String,
    onBack: (() -> Unit)? = null,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    isHighContrast: Boolean,
    onToggleHighContrast: () -> Unit,
    isBigText: Boolean,
    onToggleBigText: () -> Unit,
    isVoiceEnabled: Boolean,
    onToggleVoice: () -> Unit,
    onSosClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isElderlyHighContrast = LocalHighContrastMode.current
    val bigTextScale = if (LocalBigTextMode.current) 1.25f else 1.0f

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Subtle Persistent Watermark Overlay: "RazifApps@nasadef®"
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(if (isElderlyHighContrast) 0.12f else 0.06f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "RazifApps@nasadef®",
                        fontSize = (42 * bigTextScale).sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.rotate(-28f)
                    )
                    Spacer(modifier = Modifier.height(180.dp))
                    Text(
                        text = "RazifApps@nasadef®",
                        fontSize = (36 * bigTextScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.rotate(-28f)
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
            ) {
                // Top App Bar with Accessibility & SOS Controls
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    shadowElevation = 4.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (onBack != null) {
                                IconButton(
                                    onClick = onBack,
                                    modifier = Modifier
                                        .size(48.dp)
                                        .testTag("appbar_back_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Kembali ke Menu",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = (17 * bigTextScale).sp
                                ),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = if (onBack == null) 12.dp else 4.dp)
                            )

                            // Voice Guide Toggle
                            IconButton(
                                onClick = onToggleVoice,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("toggle_voice_guide_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                    contentDescription = "Panduan Suara",
                                    tint = if (isVoiceEnabled) MaterialTheme.colorScheme.primary else Color.Gray
                                )
                            }

                            // Big Text Toggle
                            IconButton(
                                onClick = onToggleBigText,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("toggle_big_text_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatSize,
                                    contentDescription = "Teks Besar",
                                    tint = if (isBigText) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // High Contrast Toggle
                            IconButton(
                                onClick = onToggleHighContrast,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("toggle_high_contrast_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Contrast,
                                    contentDescription = "Mod Kontras Tinggi",
                                    tint = if (isHighContrast) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            // Dark Mode Toggle
                            IconButton(
                                onClick = onToggleDarkMode,
                                modifier = Modifier
                                    .size(48.dp)
                                    .testTag("toggle_dark_mode_button")
                            ) {
                                Icon(
                                    imageVector = if (isDarkMode) Icons.Default.Brightness7 else Icons.Default.Brightness4,
                                    contentDescription = "Tukar Mod Gelap/Cerah",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // One-Touch SOS Emergency Button
                            ElevatedButton(
                                onClick = onSosClick,
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color(0xFFEF4444),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .height(40.dp)
                                    .testTag("topbar_sos_emergency_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "SOS",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "SOS",
                                    fontWeight = FontWeight.Black,
                                    fontSize = (13 * bigTextScale).sp
                                )
                            }
                        }
                    }
                }

                // Main Content Body
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    content()
                }

                // Mandatory Persistent Footer on ALL pages with active link to https://nasadef.com.my
                Surface(
                    color = if (isElderlyHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .clickable {
                            val intent = Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://nasadef.com.my")
                            )
                            context.startActivity(intent)
                        }
                        .testTag("nasadef_footer_link")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "©COPYRIGHT BY NASADEF™ SDN. BHD.®",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontSize = (11.5f * bigTextScale).sp
                            ),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "nasadef.com.my",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = (11f * bigTextScale).sp
                                ),
                                textDecoration = TextDecoration.Underline,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.Default.OpenInBrowser,
                                contentDescription = "Buka Laman Web Nasadef",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
