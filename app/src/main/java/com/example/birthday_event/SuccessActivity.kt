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
import com.example.birthday_event.databinding.ActivitySuccessBinding
import java.util.Random

class SuccessActivity : BaseActivity() {
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
        // 1단계: 카드 삽입 시 기계음
        SoundUtil.playCardMechanical()
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

        // 현금 인출 중... (지폐 세는 소리 3초간 재생)
        SoundUtil.playMoneyCounting(3000)

        // 3초간 로딩 후 4단계(돈다발) 시작
        handler.postDelayed({
            binding.layoutLoading.visibility = View.GONE
            startMoneyRain()
        }, 3000)
    }

    private fun startMoneyRain() {
        isAnimating = true
        // 4단계: 돈 내릴 때 팡파르와 함께 BGM 시작!
        SoundUtil.playSuccessFanfare()
        BgmManager.start(this) // BGM 재생 시작

        // 주기적으로 돈(지폐) 생성
        handler.post(object : Runnable {
            override fun run() {
                if (isAnimating) {
                    createBanknote()
                    handler.postDelayed(this, 80)
                }
            }
        })
    }

    private fun createBanknote() {
        val banknote = ImageView(this).apply {
            setImageResource(R.drawable.ic_banknote)
            val size = random.nextInt(100) + 120
            layoutParams = ViewGroup.LayoutParams(size, size / 2)
        }

        val screenWidth = binding.container.width
        val screenHeight = binding.container.height
        if (screenWidth <= 0 || screenHeight <= 0) return

        banknote.x = random.nextInt(screenWidth).toFloat()
        banknote.y = -200f
        banknote.rotation = random.nextInt(360).toFloat()

        binding.container.addView(banknote)

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

    override fun onResume() {
        super.onResume()
        // 애니메이션 중이라면 BGM 재개 (앱 복귀 시)
        if (isAnimating) {
            BgmManager.resume()
        }
    }

    override fun onPause() {
        super.onPause()
        // 홈 버튼 등을 누를 때 BGM 일시정지
        BgmManager.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        isAnimating = false
        handler.removeCallbacksAndMessages(null)
        // 화면이 닫힐 때 BGM 완전 정지 및 해제
        BgmManager.stop()
    }
}
