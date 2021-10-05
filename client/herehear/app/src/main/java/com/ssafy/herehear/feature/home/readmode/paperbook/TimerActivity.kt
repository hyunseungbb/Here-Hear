package com.ssafy.herehear.feature.home.readmode.paperbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message

import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityCamera2Binding
import com.ssafy.herehear.databinding.ActivityTimerBinding
import com.ssafy.herehear.feature.home.readmode.CommentActivity
import com.ssafy.herehear.util.GlideApp
import java.util.*
import kotlin.concurrent.timer

class TimerActivity : AppCompatActivity() {
    val binding by lazy { ActivityTimerBinding.inflate(layoutInflater) }
    private var sec = 0
    private var min = 0
    private var timerTask: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        startTimer()
        binding.stopButton.setOnClickListener {
            stopTimer()
            // 책 아이디, 독서시간 가지고 감상문 작성하러가기
            val bookId = intent.getIntExtra("bookId", 0)
            val commentIntent = Intent(this, CommentActivity::class.java)
            commentIntent.putExtra("time", min+1)
            commentIntent.putExtra("bookId", bookId)
            startActivity(commentIntent)
            finish()
        }
        val bookImgUrl = intent.getStringExtra("bookImgUrl")
        GlideApp.with(binding.timerImageView).load(bookImgUrl)
            .into(binding.timerImageView)

        binding.timerButton.setOnClickListener {
            if (binding.timerButton.isSelected) {
                startTimer()
            } else {
                stopTimer()
                binding.timerView.text = getString(R.string.read_mode_stop)
            }
            binding.timerButton.isSelected = !binding.timerButton.isSelected
        }
    }

    private fun startTimer() {
        timerTask = timer(period = 1000) {

            sec++
            if (sec == 60) {
                min++
                sec = 0
//                runOnUiThread {
//                    //화면에 표시해주면된다
//                }
            }
            // 일단은 빨리 확인하기 위해 초단위로
            runOnUiThread {
                binding.timerView.text = getString(R.string.read_mode, sec.toString())
            }
        }
    }

    private fun stopTimer() {
        timerTask?.cancel()
    }

}