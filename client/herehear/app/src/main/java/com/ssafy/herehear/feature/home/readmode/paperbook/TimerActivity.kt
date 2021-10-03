package com.ssafy.herehear.feature.home.readmode.paperbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.herehear.databinding.ActivityCamera2Binding
import com.ssafy.herehear.databinding.ActivityTimerBinding

class TimerActivity : AppCompatActivity() {
    val binding by lazy { ActivityTimerBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}