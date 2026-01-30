package com.example.birthday_event

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.birthday_event.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private var inputPin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupKeypad()
    }

    private fun setupKeypad() {
        val buttons = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                if (inputPin.length < 4) {
                    inputPin += (it as Button).text
                    updatePinIndicator()
                    if (inputPin.length == 4) {
                        checkPassword()
                    }
                }
            }
        }

        binding.btnDelete.setOnClickListener {
            if (inputPin.isNotEmpty()) {
                inputPin = inputPin.substring(0, inputPin.length - 1)
                updatePinIndicator()
            }
        }
    }

    private fun updatePinIndicator() {
        val indicator = StringBuilder()
        for (i in 0 until 4) {
            if (i < inputPin.length) {
                indicator.append("● ")
            } else {
                indicator.append("○ ")
            }
        }
        binding.tvPinIndicator.text = indicator.toString().trim()
    }

    private fun checkPassword() {
        if (inputPin == "1234") {
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Shake animation
            val shake = AnimationUtils.loadAnimation(this, android.R.anim.fade_in) // In real case, use a custom shake animation
            binding.tvPinIndicator.startAnimation(shake)
            
            // Vibration
            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(300)
            }

            Toast.makeText(this, "비밀번호가 틀렸습니다!", Toast.LENGTH_SHORT).show()
            inputPin = ""
            updatePinIndicator()
        }
    }
}
