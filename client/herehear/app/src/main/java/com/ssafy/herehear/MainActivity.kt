package com.ssafy.herehear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}