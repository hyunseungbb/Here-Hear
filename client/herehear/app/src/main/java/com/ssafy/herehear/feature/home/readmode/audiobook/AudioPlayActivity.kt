package com.ssafy.herehear.feature.home.readmode.audiobook

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityAudioPlayBinding
import com.ssafy.herehear.databinding.ActivityCamera2Binding

class AudioPlayActivity : AppCompatActivity() {

    val binding by lazy { ActivityAudioPlayBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }
}