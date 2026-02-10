package com.example.birthday_event

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.birthday_event.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bgmPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // BGM 플레이어 초기화 및 재생
        try {
            bgmPlayer = MediaPlayer.create(this, R.raw.birthday_bgm)
            bgmPlayer?.isLooping = true // 무한 반복
        } catch (e: Exception) {
            // R.raw.birthday_bgm 파일이 없거나 오류 발생 시 앱이 죽지 않도록 예외 처리
            bgmPlayer = null
        }

        binding.btnNext.setOnClickListener {
            SoundUtil.playBeep()
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 화면이 다시 보일 때 BGM 재생 (일시정지 상태였다면)
        if (bgmPlayer?.isPlaying == false) {
            bgmPlayer?.start()
        }
    }

    override fun onPause() {
        super.onPause()
        // 다른 화면으로 이동하거나 홈 버튼을 누를 때 BGM 일시정지
        bgmPlayer?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 앱이 완전히 종료될 때 BGM 리소스 해제
        bgmPlayer?.stop()
        bgmPlayer?.release()
        bgmPlayer = null
    }
}
