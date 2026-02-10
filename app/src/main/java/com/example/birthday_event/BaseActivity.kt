package com.example.birthday_event

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onResume() {
        super.onResume()
        // 어떤 화면이든 사용자가 보고 있다면 BGM 재생
        BgmManager.start(this)
    }

    override fun onPause() {
        super.onPause()
        // 화면이 가려질 때 일시정지 (앱 전체가 백그라운드로 갈 때를 대비)
        // 하지만 다른 Activity로 바로 이동하는 경우, 다음 Activity의 onResume에서 다시 start되므로 끊김 없이 들립니다.
        BgmManager.pause()
    }
}
