package com.example.services

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import com.example.data.model.ScanFilter

object DocumentEngine {

    /**
     * Generates a high-resolution document bitmap simulating official forms, IC cards, or certificates
     */
    fun createSampleDocumentBitmap(title: String, subtitle: String, isIdCard: Boolean = false, pageNumber: Int = 1): Bitmap {
        val width = if (isIdCard) 800 else 700
        val height = if (isIdCard) 500 else 1000
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Background
        val bgPaint = Paint().apply {
            color = if (isIdCard) Color.parseColor("#E0F2FE") else Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Border
        val borderPaint = Paint().apply {
            color = if (isIdCard) Color.parseColor("#0284C7") else Color.parseColor("#334155")
            style = Paint.Style.STROKE
            strokeWidth = if (isIdCard) 8f else 4f
        }
        canvas.drawRect(12f, 12f, width - 12f, height - 12f, borderPaint)

        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#0F172A")
            textSize = if (isIdCard) 28f else 32f
            isFakeBoldText = true
        }

        if (isIdCard) {
            // Header for Malaysian IC Style
            val headerPaint = Paint().apply {
                color = Color.parseColor("#0369A1")
                style = Paint.Style.FILL
            }
            canvas.drawRect(20f, 20f, width - 20f, 80f, headerPaint)

            val headerTextPaint = Paint().apply {
                color = Color.WHITE
                textSize = 24f
                isFakeBoldText = true
                isAntiAlias = true
            }
            canvas.drawText("KERAJAAN MALAYSIA • KAD PENGENALAN DOKUMEN RASMI", 40f, 58f, headerTextPaint)

            // Photo box placeholder
            val photoPaint = Paint().apply {
                color = Color.parseColor("#CBD5E1")
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(RectF(40f, 110f, 220f, 350f), 12f, 12f, photoPaint)

            val photoTextPaint = Paint().apply {
                color = Color.parseColor("#475569")
                textSize = 20f
                isAntiAlias = true
            }
            canvas.drawText("FOTO", 100f, 230f, photoTextPaint)
            canvas.drawText("PEMEGANG", 70f, 260f, photoTextPaint)

            // IC Details
            val detailPaint = Paint().apply {
                color = Color.parseColor("#0F172A")
                textSize = 22f
                isAntiAlias = true
            }
            val boldDetailPaint = Paint().apply {
                color = Color.parseColor("#0F172A")
                textSize = 24f
                isFakeBoldText = true
                isAntiAlias = true
            }

            canvas.drawText("NO. K/P: 780512-10-5421", 250f, 140f, boldDetailPaint)
            canvas.drawText("NAMA: AHMAD RAZIF BIN ABDULLAH", 250f, 185f, boldDetailPaint)
            canvas.drawText("ALAMAT: NO. 45, JALAN MERBAU 3,", 250f, 230f, detailPaint)
            canvas.drawText("50400 KUALA LUMPUR, W.P.", 250f, 265f, detailPaint)
            canvas.drawText("WARGANEGARA: MALAYSIA", 250f, 310f, detailPaint)
            canvas.drawText("STATUS: PENDUDUK WARGA EMAS / BANTUAN AKTIF", 250f, 350f, detailPaint)

            // Microchip Simulation
            val chipPaint = Paint().apply {
                color = Color.parseColor("#F59E0B")
                style = Paint.Style.FILL
            }
            canvas.drawRoundRect(RectF(40f, 370f, 130f, 440f), 8f, 8f, chipPaint)

            // Security seal
            val sealPaint = Paint().apply {
                color = Color.parseColor("#0284C7")
                style = Paint.Style.STROKE
                strokeWidth = 3f
            }
            canvas.drawCircle(680f, 380f, 60f, sealPaint)
            val sealText = Paint().apply {
                color = Color.parseColor("#0284C7")
                textSize = 14f
                isAntiAlias = true
            }
            canvas.drawText("SAH", 665f, 375f, sealText)
            canvas.drawText("KERAJAAN", 645f, 395f, sealText)

        } else {
            // Official Document / Form Header
            val emblemPaint = Paint().apply {
                color = Color.parseColor("#0284C7")
                style = Paint.Style.FILL
            }
            canvas.drawCircle(width / 2f, 80f, 35f, emblemPaint)

            val emblemTextPaint = Paint().apply {
                color = Color.WHITE
                textSize = 16f
                isFakeBoldText = true
                isAntiAlias = true
            }
            canvas.drawText("MY", width / 2f - 14f, 86f, emblemTextPaint)

            // Title
            textPaint.textAlign = Paint.Align.CENTER
            canvas.drawText(title, width / 2f, 160f, textPaint)

            val subPaint = Paint().apply {
                color = Color.parseColor("#64748B")
                textSize = 20f
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            canvas.drawText(subtitle, width / 2f, 195f, subPaint)
            canvas.drawText("Halaman $pageNumber daripada 2 • Kiosk Cetakan Rasmi", width / 2f, 225f, subPaint)

            // Divider line
            val linePaint = Paint().apply {
                color = Color.parseColor("#CBD5E1")
                strokeWidth = 3f
            }
            canvas.drawLine(50f, 245f, width - 50f, 245f, linePaint)

            // Document body paragraphs & simulated text lines
            val bodyPaint = Paint().apply {
                color = Color.parseColor("#1E293B")
                textSize = 20f
                isAntiAlias = true
            }
            canvas.drawText("BAHAGIAN A: BUTIRAN PERMOHONAN & MAKLUMAT PEMOHON", 50f, 285f, Paint(bodyPaint).apply { isFakeBoldText = true })

            var y = 330f
            val sampleLines = listOf(
                "1. Nama Penuh: ________________________________________________",
                "2. Nombor Kad Pengenalan: [  ][  ][  ][  ][  ][  ] - [  ][  ] - [  ][  ][  ][  ]",
                "3. Tarikh Permohonan: 14 Oktober 2026",
                "4. No. Rujukan Fail Kiosk: KSK-MY-2026-88741",
                "5. Kategori Kebajikan / OKU / Warga Emas: Diperakui Berdaftar",
                "6. Alamat Kediaman: Petaling Jaya, Selangor Darul Ehsan",
                "7. Nombor Telefon Waris / Kecemasan: 017-09971963",
                "8. Status Bayaran Yuran Cetakan: Selesai Secara Nirtunai DuitNow",
                "9. Perakuan Pemohon: Saya mengesahkan bahawa maklumat di atas adalah benar."
            )

            for (line in sampleLines) {
                canvas.drawText(line, 50f, y, bodyPaint)
                y += 45f
            }

            // Signature & Cop Rasmi Boxes
            val boxPaint = Paint().apply {
                color = Color.parseColor("#94A3B8")
                style = Paint.Style.STROKE
                strokeWidth = 2f
            }
            canvas.drawRect(50f, y + 40f, 320f, y + 160f, boxPaint)
            canvas.drawRect(370f, y + 40f, width - 50f, y + 160f, boxPaint)

            val boxText = Paint().apply {
                color = Color.parseColor("#64748B")
                textSize = 16f
                isAntiAlias = true
            }
            canvas.drawText("Tandatangan Pemohon", 70f, y + 70f, boxText)
            canvas.drawText("Cop Pengesahan Kiosk & Tarikh", 390f, y + 70f, boxText)

            // Barcode at bottom
            val barcodePaint = Paint().apply {
                color = Color.BLACK
                strokeWidth = 4f
            }
            var bx = 150f
            while (bx < width - 150f) {
                val thick = (bx.toInt() % 7 == 0) || (bx.toInt() % 13 == 0)
                barcodePaint.strokeWidth = if (thick) 6f else 2f
                canvas.drawLine(bx, height - 70f, bx, height - 25f, barcodePaint)
                bx += 8f
            }
        }

        return bitmap
    }

    /**
     * Applies High-Resolution Scanner filters: High-Contrast B&W Photostat, Grayscale, Sharpen
     */
    fun applyScanFilter(original: Bitmap, filter: ScanFilter): Bitmap {
        if (filter == ScanFilter.ORIGINAL) return original

        val width = original.width
        val height = original.height
        val filtered = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val pixels = IntArray(width * height)
        original.getPixels(pixels, 0, width, 0, 0, width, height)

        when (filter) {
            ScanFilter.PHOTOSTAT_BW -> {
                // High-Contrast thresholding for photostat
                val threshold = 145
                for (i in pixels.indices) {
                    val p = pixels[i]
                    val r = (p shr 16) and 0xFF
                    val g = (p shr 8) and 0xFF
                    val b = p and 0xFF
                    // Luminance formula
                    val lum = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
                    val bw = if (lum > threshold) 255 else 0
                    pixels[i] = (0xFF shl 24) or (bw shl 16) or (bw shl 8) or bw
                }
            }
            ScanFilter.GRAYSCALE -> {
                for (i in pixels.indices) {
                    val p = pixels[i]
                    val r = (p shr 16) and 0xFF
                    val g = (p shr 8) and 0xFF
                    val b = p and 0xFF
                    val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
                    pixels[i] = (0xFF shl 24) or (gray shl 16) or (gray shl 8) or gray
                }
            }
            ScanFilter.ENHANCED_SHARP -> {
                // High contrast saturation enhancement
                for (i in pixels.indices) {
                    val p = pixels[i]
                    var r = (p shr 16) and 0xFF
                    var g = (p shr 8) and 0xFF
                    var b = p and 0xFF

                    // Contrast boost
                    r = ((r - 128) * 1.35 + 128).toInt().coerceIn(0, 255)
                    g = ((g - 128) * 1.35 + 128).toInt().coerceIn(0, 255)
                    b = ((b - 128) * 1.35 + 128).toInt().coerceIn(0, 255)

                    pixels[i] = (0xFF shl 24) or (r shl 16) or (g shl 8) or b
                }
            }
            else -> {}
        }

        filtered.setPixels(pixels, 0, width, 0, 0, width, height)
        return filtered
    }
}
