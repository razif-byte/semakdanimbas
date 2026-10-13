package com.example.services

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiChatService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun getAiResponse(userPrompt: String, history: List<Pair<String, String>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If key is present and valid, call real Gemini REST API
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

                val contentsArray = JSONArray()

                // System instruction for Kiosk Assistant
                val systemInstructionText = """
                    Anda adalah Pembantu Pintar AI Kiosk Percetakan & Fotostat Layan Diri (Nasadef Sdn Bhd).
                    Jawab soalan pengguna dengan ramah, sopan, ringkas, dan jelas dalam Bahasa Melayu.
                    Maklumat Kiosk:
                    - Servis: Cetak Dokumen PDF, Fotostat Kad Pengenalan & Dokumen, Pengimbas Resolusi Tinggi.
                    - Kadar Harga: Hitam Putih RM0.20/helai (A4), Warna RM0.80/helai (A4), A3 RM0.40(BW) / RM1.60(Warna).
                    - Pembayaran nirtunai: CIMB DuitNow (7016657934) & TNG DuitNow (170997196374).
                    - Ciri mesra OKU & Warga Emas: Butang SOS 1-sentuhan kecemasan, panduan suara, teks besar, kontras tinggi, peringatan ubat harian, dan log masuk biometrik mudah.
                    - Pemilik Sistem: NASADEF SDN BHD (https://nasadef.com.my).
                    Bimbing pengguna langkah demi langkah jika mereka bertanya cara penggunaan.
                """.trimIndent()

                // Build request contents
                val systemPart = JSONObject().put("text", systemInstructionText)
                val systemContent = JSONObject().put("parts", JSONArray().put(systemPart))

                // Append conversation history
                for ((role, text) in history.takeLast(4)) {
                    val partObj = JSONObject().put("text", text)
                    val contentObj = JSONObject()
                        .put("role", if (role == "user") "user" else "model")
                        .put("parts", JSONArray().put(partObj))
                    contentsArray.put(contentObj)
                }

                // Append current user message
                val currentPart = JSONObject().put("text", userPrompt)
                val currentContent = JSONObject()
                    .put("role", "user")
                    .put("parts", JSONArray().put(currentPart))
                contentsArray.put(currentContent)

                val requestJson = JSONObject()
                    .put("contents", contentsArray)
                    .put("systemInstruction", systemContent)

                val body = requestJson.toString().toRequestBody(jsonMediaType)
                val request = Request.Builder()
                    .url(url)
                    .post(body)
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: ""

                if (response.isSuccessful) {
                    val root = JSONObject(responseBody)
                    val candidates = root.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val content = firstCandidate.getJSONObject("content")
                        val parts = content.getJSONArray("parts")
                        if (parts.length() > 0) {
                            val reply = parts.getJSONObject(0).getString("text")
                            return@withContext reply.trim()
                        }
                    }
                } else {
                    Log.w("GeminiChatService", "Gemini API error code: ${response.code}, body: $responseBody")
                }
            } catch (e: Exception) {
                Log.e("GeminiChatService", "Gemini API call failed: ${e.message}")
            }
        }

        // Contextual Fallback Knowledge Engine (always accurate, offline-ready)
        return@withContext generateSmartFallbackReply(userPrompt)
    }

    private fun generateSmartFallbackReply(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("harga") || lower.contains("kadar") || lower.contains("kos") || lower.contains("bayar berapa") -> {
                "Kadar harga kiosk percetakan kami adalah sangat berpatutan:\n" +
                "• Hitam Putih (A4): RM 0.20 sehelai\n" +
                "• Berwarna Penuh (A4): RM 0.80 sehelai\n" +
                "• Saiz A3 (Hitam Putih): RM 0.40 sehelai\n" +
                "• Saiz A3 (Warna): RM 1.60 sehelai\n" +
                "Pembayaran boleh dibuat melalui DuitNow QR (CIMB & TNG) secara nirtunai."
            }
            lower.contains("bayar") || lower.contains("duitnow") || lower.contains("tng") || lower.contains("cimb") || lower.contains("qr") -> {
                "Sistem kami menyokong pembayaran nirtunai segera melalui DuitNow QR:\n" +
                "1. CIMB DuitNow: 7016657934 (NASADEF SDN BHD)\n" +
                "2. TNG DuitNow: 170997196374 (NASADEF KIOSK)\n" +
                "Buka aplikasi perbankan atau e-dompet anda, imbas kod QR di skrin dan sahkan bayaran. Status akan dikemaskini dalam masa nyata!"
            }
            lower.contains("ic") || lower.contains("kad pengenalan") || lower.contains("fotostat") || lower.contains("salinan") -> {
                "Untuk membuat fotostat kad pengenalan (IC) atau dokumen rasmi:\n" +
                "1. Tekan butang 'Fotostat & Pengimbas Resolusi Tinggi' di menu utama.\n" +
                "2. Letakkan IC anda pada pengimbas.\n" +
                "3. Pilih mod penapis 'Fotostat Hitam & Putih Berkontras Tinggi' untuk teks yang sangat tajam.\n" +
                "4. Lihat pratonton dan sahkan bilangan salinan sebelum cetak."
            }
            lower.contains("pdf") || lower.contains("cetak") || lower.contains("print") || lower.contains("fail") -> {
                "Anda boleh mencetak fail PDF secara langsung:\n" +
                "1. Pilih menu 'Cetak Dokumen'.\n" +
                "2. Pilih dokumen rasmi tersedia atau pilih fail PDF dari peranti anda.\n" +
                "3. Gunakan 'Pratonton Cetakan' untuk melihat helaian sebelum membayar.\n" +
                "4. Pilih saiz kertas (A4, A3, Legal) dan bilangan salinan."
            }
            lower.contains("warga emas") || lower.contains("oku") || lower.contains("suara") || lower.contains("teks") || lower.contains("kontras") -> {
                "Kiosk ini direka khas untuk kemudahan Warga Emas & OKU:\n" +
                "• Panduan Suara (TTS): Tekan ikon pembesar suara untuk mendengar arahan.\n" +
                "• Teks Besar: Naikkan saiz teks dengan menekan butang 'Teks Besar' di atas skrin.\n" +
                "• Mod Kontras Tinggi: Tekan butang 'Kontras Tinggi' untuk paparan warna kuning/hitam yang jelas.\n" +
                "• Bantuan Kecemasan SOS: Tekan butang merah SOS jika perlukan bantuan segera staf."
            }
            lower.contains("sos") || lower.contains("kecemasan") || lower.contains("bantuan") || lower.contains("sakit") -> {
                "Untuk sebarang kecemasan, tekan butang merah SOS di bahagian atas atau menu utama. Siren amaran akan berbunyi dan talian kecemasan 999 serta staf pengurusan kiosk (017-0997196374) akan dihubungi serta-merta."
            }
            lower.contains("ubat") || lower.contains("peringatan") || lower.contains("kesihatan") -> {
                "Fungsi Peringatan Ubat Harian tersedia di menu utama. Anda boleh menetapkan jadual ubat harian (pagi, petang, malam) dan sistem akan membunyikan penggera audio automatik untuk memastikan anda tidak terlepas dos ubat."
            }
            lower.contains("lokasi") || lower.contains("keluarga") || lower.contains("share") -> {
                "Anda boleh berkongsi lokasi kiosk anda secara langsung dengan keluarga melalui WhatsApp atau SMS dengan menekan menu 'Perkongsian Lokasi Keluarga'. Pautan Google Maps akan dihantar secara automatik."
            }
            else -> {
                "Salam sejahtera! Saya Pembantu AI Kiosk Cetak & Fotostat NASADEF. Saya sedia membantu anda berkaitan cetakan PDF, fotostat dokumen resolusi tinggi, bayaran nirtunai DuitNow (CIMB & TNG), mod warga emas/OKU, atau bantuan kecemasan. Ada apa-apa yang boleh saya bantu?"
            }
        }
    }
}
