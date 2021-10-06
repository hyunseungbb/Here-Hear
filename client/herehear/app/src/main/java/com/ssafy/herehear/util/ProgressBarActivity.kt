package com.ssafy.herehear.util

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.herehear.databinding.ActivityProgressBarBinding


class ProgressBarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProgressBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProgressBarBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}