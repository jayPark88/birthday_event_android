package com.example.birthday_event

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.concurrent.thread
import kotlin.math.sin

object BgmPlayer {
    private var isPlaying = false
    private const val SAMPLE_RATE = 44100

    // 음계별 주파수
    private val notes = mapOf(
        "C4" to 261.63, "D4" to 293.66, "E4" to 329.63, "F4" to 349.23,
        "G4" to 392.00, "A4" to 440.00, "B4" to 493.88, "C5" to 523.25,
        "Bb4" to 466.16
    )

    // 생일 축하 노래 악보 (음계, 박자)
    private val melody = listOf(
        "C4" to 0.5, "C4" to 0.5, "D4" to 1.0, "C4" to 1.0, "F4" to 1.0, "E4" to 2.0,
        "C4" to 0.5, "C4" to 0.5, "D4" to 1.0, "C4" to 1.0, "G4" to 1.0, "F4" to 2.0,
        "C4" to 0.5, "C4" to 0.5, "C5" to 1.0, "A4" to 1.0, "F4" to 1.0, "E4" to 1.0, "D4" to 2.0,
        "Bb4" to 0.5, "Bb4" to 0.5, "A4" to 1.0, "F4" to 1.0, "G4" to 1.0, "F4" to 2.0
    )

    fun start() {
        if (isPlaying) return
        isPlaying = true
        
        thread {
            val minBufferSize = AudioTrack.getMinBufferSize(
                SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT
            )
            val audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                minBufferSize, AudioTrack.MODE_STREAM
            )

            audioTrack.play()

            while (isPlaying) {
                for (note in melody) {
                    if (!isPlaying) break
                    val frequency = notes[note.first] ?: 0.0
                    val duration = (note.second * 400).toInt() // 속도 조절
                    playTone(audioTrack, frequency, duration)
                    Thread.sleep(50) // 음 간격
                }
                Thread.sleep(1000) // 반복 전 대기
            }
            
            audioTrack.stop()
            audioTrack.release()
        }
    }

    fun stop() {
        isPlaying = false
    }

    private fun playTone(audioTrack: AudioTrack, frequency: Double, durationMs: Int) {
        val count = (SAMPLE_RATE * durationMs / 1000.0).toInt()
        val samples = ShortArray(count)
        for (i in 0 until count) {
            samples[i] = (sin(2.0 * Math.PI * i / (SAMPLE_RATE / frequency)) * Short.MAX_VALUE).toInt().toShort()
        }
        audioTrack.write(samples, 0, count)
    }
}
