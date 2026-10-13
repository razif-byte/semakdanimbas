package com.example.ui.screens

import android.graphics.Bitmap
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EWalletProvider
import com.example.data.model.PaymentStatus
import com.example.data.model.PaymentTransaction
import com.example.data.model.PrintJob
import com.example.services.DuitNowQrEngine
import com.example.ui.theme.LocalBigTextMode
import com.example.ui.theme.LocalHighContrastMode
import kotlinx.coroutines.delay

@Composable
fun PaymentScreen(
    currentJob: PrintJob,
    onPaymentSuccess: (PaymentTransaction) -> Unit,
    onSpeak: (String) -> Unit
) {
    val isBigText = LocalBigTextMode.current
    val isHighContrast = LocalHighContrastMode.current
    val textScale = if (isBigText) 1.25f else 1.0f

    var selectedProvider by remember { mutableStateOf(EWalletProvider.CIMB_DUITNOW) }
    var paymentStatus by remember { mutableStateOf(PaymentStatus.WAITING_SCAN) }
    var secondsLeft by remember { mutableIntStateOf(180) }

    // Countdown Timer
    LaunchedEffect(paymentStatus) {
        if (paymentStatus == PaymentStatus.WAITING_SCAN) {
            while (secondsLeft > 0 && paymentStatus == PaymentStatus.WAITING_SCAN) {
                delay(1000)
                secondsLeft--
            }
        }
    }

    // Dynamic QR bitmap for selected provider
    val qrBitmap: Bitmap = remember(selectedProvider, currentJob.totalPrice) {
        DuitNowQrEngine.generateDuitNowQr(selectedProvider, currentJob.totalPrice)
    }

    val transaction = remember(selectedProvider, currentJob.totalPrice) {
        PaymentTransaction(
            provider = selectedProvider,
            amount = currentJob.totalPrice
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("payment_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // E-Wallet Account Selection Tabs
        item {
            Text(
                text = "PILIH KAEDAH PEMBAYARAN NIRITUNAI DUITNOW",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = (14 * textScale).sp),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // CIMB Bank DuitNow
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedProvider == EWalletProvider.CIMB_DUITNOW) {
                            Color(0xFFED008C).copy(alpha = 0.15f)
                        } else {
                            if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = BorderStroke(
                        width = if (selectedProvider == EWalletProvider.CIMB_DUITNOW) 2.dp else 1.dp,
                        color = if (selectedProvider == EWalletProvider.CIMB_DUITNOW) Color(0xFFED008C) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedProvider = EWalletProvider.CIMB_DUITNOW
                            onSpeak("Kaedah pembayaran CIMB DuitNow dipilih. Nombor akaun 7016657934.")
                        }
                        .testTag("tab_cimb_duitnow")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = "CIMB",
                            tint = Color(0xFFED008C),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "CIMB Bank",
                            fontSize = (14 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "7016657934",
                            fontSize = (12 * textScale).sp,
                            color = Color(0xFFED008C),
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // TNG eWallet DuitNow
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (selectedProvider == EWalletProvider.TNG_DUITNOW) {
                            Color(0xFF0176D3).copy(alpha = 0.15f)
                        } else {
                            if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = BorderStroke(
                        width = if (selectedProvider == EWalletProvider.TNG_DUITNOW) 2.dp else 1.dp,
                        color = if (selectedProvider == EWalletProvider.TNG_DUITNOW) Color(0xFF0176D3) else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedProvider = EWalletProvider.TNG_DUITNOW
                            onSpeak("Kaedah pembayaran Touch n Go DuitNow dipilih. Nombor akaun 170997196374.")
                        }
                        .testTag("tab_tng_duitnow")
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = "TNG",
                            tint = Color(0xFF0176D3),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "TNG DuitNow",
                            fontSize = (14 * textScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighContrast) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "170997196374",
                            fontSize = (12 * textScale).sp,
                            color = Color(0xFF0176D3),
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        // Live DuitNow QR Code Box
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighContrast) Color(0xFF1E293B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(2.dp, if (selectedProvider == EWalletProvider.CIMB_DUITNOW) Color(0xFFED008C) else Color(0xFF0176D3)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("duitnow_qr_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Timer & Live Gateway Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Masa",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            val minutes = secondsLeft / 60
                            val seconds = secondsLeft % 60
                            Text(
                                text = "Tamat: ${String.format("%02d:%02d", minutes, seconds)}",
                                fontSize = (13 * textScale).sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (paymentStatus) {
                                PaymentStatus.WAITING_SCAN -> Color(0xFFF59E0B).copy(alpha = 0.2f)
                                PaymentStatus.VERIFYING -> Color(0xFF3B82F6).copy(alpha = 0.2f)
                                PaymentStatus.SUCCESS -> Color(0xFF10B981).copy(alpha = 0.2f)
                                PaymentStatus.FAILED -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                PaymentStatus.IDLE -> Color.Gray.copy(alpha = 0.2f)
                            }
                        ) {
                            Text(
                                text = when (paymentStatus) {
                                    PaymentStatus.WAITING_SCAN -> "Menunggu Imbasan E-Wallet"
                                    PaymentStatus.VERIFYING -> "Mengesahkan Gateway..."
                                    PaymentStatus.SUCCESS -> "BAYARAN BERJAYA"
                                    PaymentStatus.FAILED -> "Gagal"
                                    PaymentStatus.IDLE -> "Sedia"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = when (paymentStatus) {
                                    PaymentStatus.WAITING_SCAN -> Color(0xFFD97706)
                                    PaymentStatus.VERIFYING -> Color(0xFF2563EB)
                                    PaymentStatus.SUCCESS -> Color(0xFF059669)
                                    PaymentStatus.FAILED -> Color(0xFFDC2626)
                                    PaymentStatus.IDLE -> Color.Gray
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // QR Code Image
                    Image(
                        bitmap = qrBitmap.asImageBitmap(),
                        contentDescription = "Kod QR DuitNow Rasmi",
                        modifier = Modifier
                            .size(270.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Imbas menggunakan mana-mana Aplikasi Bank atau Touch 'n Go eWallet",
                        fontSize = (13 * textScale).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "No Rujukan: ${transaction.referenceNo}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Action Buttons: Real-Time Verify Simulation & Cashless Confirmation
        item {
            if (paymentStatus != PaymentStatus.SUCCESS) {
                Button(
                    onClick = {
                        paymentStatus = PaymentStatus.VERIFYING
                        onSpeak("Menyemak status bayaran masuk dari gateway DuitNow...")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedProvider == EWalletProvider.CIMB_DUITNOW) Color(0xFFED008C) else Color(0xFF0176D3)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("btn_verify_payment")
                ) {
                    if (paymentStatus == PaymentStatus.VERIFYING) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text("Mengesahkan Bayaran API...", fontWeight = FontWeight.Bold)
                    } else {
                        Icon(Icons.Default.Refresh, contentDescription = "Semak")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sahkan Bayaran Selesai (Simulasi Gateway)",
                            fontWeight = FontWeight.Bold,
                            fontSize = (14 * textScale).sp
                        )
                    }
                }

                // Auto-complete simulation if user clicks verify
                if (paymentStatus == PaymentStatus.VERIFYING) {
                    LaunchedEffect(Unit) {
                        delay(2000)
                        paymentStatus = PaymentStatus.SUCCESS
                        onSpeak("Bayaran RM ${String.format("%.2f", currentJob.totalPrice)} berjaya disahkan! Memulakan proses cetakan.")
                    }
                }
            } else {
                // Success State Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF10B981).copy(alpha = 0.15f)),
                    border = BorderStroke(2.dp, Color(0xFF10B981)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Berjaya",
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Bayaran Diterima & Disahkan!",
                            fontSize = (18 * textScale).sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF059669)
                        )
                        Text(
                            text = "Resit Elektronik Kiosk telah dijana.",
                            fontSize = (13 * textScale).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                onPaymentSuccess(transaction.copy(status = PaymentStatus.SUCCESS))
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_start_print_job")
                        ) {
                            Icon(Icons.Default.Print, contentDescription = "Cetak")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Mula Mencetak Dokumen Sekarang",
                                fontWeight = FontWeight.Bold,
                                fontSize = (15 * textScale).sp
                            )
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
