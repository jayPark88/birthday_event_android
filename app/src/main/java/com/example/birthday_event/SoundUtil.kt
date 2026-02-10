package com.example.birthday_event

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.concurrent.thread
import kotlin.math.sin

object SoundUtil {
    private const val SAMPLE_RATE = 44100

    // 1. 케주얼한 '톡' 소리 (버튼 클릭)
    fun playBeep() {
        playTone(880.0, 50, 0.3) // 높은 미음, 짧게
    }

    // 2. 부드러운 기계 작동음 (카드 삽입)
    fun playCardMechanical() {
        thread {
            for (i in 0..2) {
                playTone(440.0 + (i * 100), 80, 0.2)
                Thread.sleep(100)
            }
        }
    }

    // 3. 지폐 세는 소리 (가벼운 '틱' 소리 반복)
    fun playMoneyCounting(durationMs: Long) {
        val startTime = System.currentTimeMillis()
        thread {
            while (System.currentTimeMillis() - startTime < durationMs) {
                playTone(1200.0, 20, 0.1) // 아주 짧고 높은 소리
                Thread.sleep(80)
            }
        }
    }

    // 4. 경쾌한 성공음 (도-미-솔-도)
    fun playConfirm() {
        thread {
            val frequencies = listOf(523.25, 659.25, 783.99, 1046.50)
            for (freq in frequencies) {
                playTone(freq, 100, 0.2)
                Thread.sleep(120)
            }
        }
    }

    // 5. 귀여운 에러음 (붑-)
    fun playError() {
        thread {
            playTone(300.0, 150, 0.3)
            Thread.sleep(50)
            playTone(200.0, 200, 0.3)
        }
    }

    // 6. 돈다발 팡파르 (화려한 아르페지오)
    fun playSuccessFanfare() {
        thread {
            val melody = listOf(783.99, 1046.50, 1318.51, 1567.98)
            for (freq in melody) {
                playTone(freq, 150, 0.2)
                Thread.sleep(100)
            }
        }
    }

    private fun playTone(frequency: Double, durationMs: Int, volume: Double) {
        val count = (SAMPLE_RATE * durationMs / 1000.0).toInt()
        val samples = ShortArray(count)
        for (i in 0 until count) {
            // Sine wave + Fade out (to avoid clicks)
            val fadeOut = (count - i).toDouble() / count
            samples[i] = (sin(2.0 * Math.PI * i / (SAMPLE_RATE / frequency)) * Short.MAX_VALUE * volume * fadeOut).toInt().toShort()
        }

        val audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC, SAMPLE_RATE,
            AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
            count * 2, AudioTrack.MODE_STATIC
        )
        audioTrack.write(samples, 0, count)
        audioTrack.play()

        // Release resources after play
        thread {
            Thread.sleep(durationMs.toLong() + 100)
            audioTrack.release()
        }
    }
}
