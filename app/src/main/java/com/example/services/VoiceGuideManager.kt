package com.example.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.Locale

class VoiceGuideManager(context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private var isInitialized = false
    var isEnabled: Boolean = true

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (e: Exception) {
            Log.e("VoiceGuideManager", "TTS initialization failed: ${e.message}")
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val malayLocale = Locale("ms", "MY")
            val result = tts?.setLanguage(malayLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default or English
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(0.95f) // slightly slower for elderly clarity
            tts?.setPitch(1.0f)
            isInitialized = true
        }
    }

    fun speak(text: String, force: Boolean = false) {
        if ((isEnabled || force) && isInitialized) {
            tts?.stop()
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "KioskVoiceGuide")
        }
    }

    fun stop() {
        if (isInitialized) {
            tts?.stop()
        }
    }

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            Log.e("VoiceGuideManager", "TTS shutdown error: ${e.message}")
        }
    }
}
