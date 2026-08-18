package com.educalab.quimicatomix.util

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Gestor de feedback sonoro y háptico, ambos OPCIONALES y silenciables desde Perfil.
 *
 * Sonido: se generan tonos cortos con [ToneGenerator] (API nativa de Android) en vez de
 * archivos de audio embebidos, evitando dependencias binarias externas mientras se ofrece
 * feedback sonoro real y funcional, 100% offline.
 *
 * Háptica: usa [VibratorManager] (API 31+) o [Vibrator] (API 24-30) con vibraciones muy
 * breves (<=40ms) para no resultar molestas.
 */
class SoundHapticsManager(context: Context) {

    private val appContext = context.applicationContext
    private var toneGenerator: ToneGenerator? = null

    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    private fun tone(): ToneGenerator? {
        if (toneGenerator == null) {
            toneGenerator = runCatching { ToneGenerator(AudioManager.STREAM_MUSIC, 60) }.getOrNull()
        }
        return toneGenerator
    }

    fun playSuccess(soundEnabled: Boolean) {
        if (!soundEnabled) return
        runCatching { tone()?.startTone(ToneGenerator.TONE_PROP_BEEP2, 140) }
    }

    fun playError(soundEnabled: Boolean) {
        if (!soundEnabled) return
        runCatching { tone()?.startTone(ToneGenerator.TONE_PROP_NACK, 160) }
    }

    fun playTap(soundEnabled: Boolean) {
        if (!soundEnabled) return
        runCatching { tone()?.startTone(ToneGenerator.TONE_PROP_BEEP, 60) }
    }

    fun playUnlock(soundEnabled: Boolean) {
        if (!soundEnabled) return
        runCatching { tone()?.startTone(ToneGenerator.TONE_PROP_ACK, 200) }
    }

    fun vibrateSuccess(hapticsEnabled: Boolean) = vibrate(hapticsEnabled, 25)
    fun vibrateError(hapticsEnabled: Boolean) = vibrate(hapticsEnabled, longArrayOf(0, 20, 40, 20))
    fun vibrateSelection(hapticsEnabled: Boolean) = vibrate(hapticsEnabled, 12)

    private fun vibrate(hapticsEnabled: Boolean, durationMs: Long) {
        if (!hapticsEnabled) return
        val v = vibrator ?: return
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(durationMs)
            }
        }
    }

    private fun vibrate(hapticsEnabled: Boolean, pattern: LongArray) {
        if (!hapticsEnabled) return
        val v = vibrator ?: return
        runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createWaveform(pattern, -1))
            } else {
                @Suppress("DEPRECATION")
                v.vibrate(pattern, -1)
            }
        }
    }

    fun release() {
        toneGenerator?.release()
        toneGenerator = null
    }
}
