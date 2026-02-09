package com.example.birthday_event

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.birthday_event.databinding.ActivitySuccessBinding
import java.util.Random

class SuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessBinding
    private val handler = Handler(Looper.getMainLooper())
    private val random = Random()
    private var isAnimating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startSequence()
    }

    private fun startSequence() {
        // 1 & 2단계: 카드 삽입 애니메이션 (5초간 유지로 수정)
        playCardInsertionAnimation()

        // 3초 후 3단계로 전환
        handler.postDelayed({
            showLoadingStage()
        }, 3000)
    }

    private fun playCardInsertionAnimation() {
        // 카드가 슬롯으로 들어갔다 나왔다 하는 반복 애니메이션
        val cardAnim = ObjectAnimator.ofFloat(binding.ivCard, "translationY", 0f, -150f)
        cardAnim.duration = 1500
        cardAnim.repeatCount = ObjectAnimator.INFINITE
        cardAnim.repeatMode = ObjectAnimator.REVERSE
        cardAnim.start()
    }

    private fun showLoadingStage() {
        // 카드 레이아웃 숨기기
        binding.layoutCardStage.visibility = View.GONE
        
        // 3단계: "현금 인출 중..." 레이아웃 표시
        binding.layoutLoading.visibility = View.VISIBLE

        // 3초간 로딩 후 4단계(돈다발) 시작
        handler.postDelayed({
            binding.layoutLoading.visibility = View.GONE
            startMoneyRain()
        }, 3000)
    }

    private fun startMoneyRain() {
        isAnimating = true
        // 4단계: 주기적으로 돈(지폐) 생성
        handler.post(object : Runnable {
            override fun run() {
                if (isAnimating) {
                    createBanknote()
                    handler.postDelayed(this, 80) // 더 화려하게 0.08초마다 생성
                }
            }
        })
    }

    private fun createBanknote() {
        val banknote = ImageView(this).apply {
            setImageResource(R.drawable.ic_banknote)
            // 랜덤한 지폐 크기
            val size = random.nextInt(100) + 120
            layoutParams = ViewGroup.LayoutParams(size, size / 2)
        }

        val screenWidth = binding.container.width
        val screenHeight = binding.container.height
        if (screenWidth <= 0 || screenHeight <= 0) return

        // 랜덤 시작 위치 및 초기 회전
        banknote.x = random.nextInt(screenWidth).toFloat()
        banknote.y = -200f
        banknote.rotation = random.nextInt(360).toFloat()

        binding.container.addView(banknote)

        // 애니메이션: 위에서 아래로 낙하 + 회전
        val duration = random.nextInt(1500) + 1500L
        val fallAnim = ObjectAnimator.ofFloat(banknote, "translationY", screenHeight.toFloat() + 200)
        val rotateAnim = ObjectAnimator.ofFloat(banknote, "rotation", banknote.rotation + 720f)
        
        fallAnim.duration = duration
        rotateAnim.duration = duration

        fallAnim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                binding.container.removeView(banknote)
            }
        })

        fallAnim.start()
        rotateAnim.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        isAnimating = false
        handler.removeCallbacksAndMessages(null)
    }
}
