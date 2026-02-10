package com.example.birthday_event

import android.content.Intent
import android.os.Bundle
import com.example.birthday_event.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            SoundUtil.playBeep()
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }
    }
}
