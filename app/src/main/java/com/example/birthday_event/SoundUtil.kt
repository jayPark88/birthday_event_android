package com.example.birthday_event

import android.media.AudioManager
import android.media.ToneGenerator

object SoundUtil {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    fun playBeep() {
        // ATM 기계 느낌의 짧고 경쾌한 비프음
        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP, 100)
    }

    fun playConfirm() {
        // 성공 시의 비프음 (약간 더 긴 소리)
        toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 200)
    }

    fun playError() {
        // 오류 시의 비프음
        toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 200)
    }
}
