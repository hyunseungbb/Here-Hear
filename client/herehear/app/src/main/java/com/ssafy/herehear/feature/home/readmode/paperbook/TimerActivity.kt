package com.ssafy.herehear.feature.home.readmode.paperbook

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.LayoutInflater
import com.bumptech.glide.Glide

import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityCamera2Binding
import com.ssafy.herehear.databinding.ActivityTimerBinding
import com.ssafy.herehear.feature.home.readmode.CommentActivity
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
            binding.timerButton.isSelected = !binding.timerButton.isSelected
            showPopup()
        }
        val bookImgUrl = intent.getStringExtra("bookImgUrl")
        Glide.with(binding.timerImageView).load(bookImgUrl)
            .into(binding.timerImageView)

        binding.timerView.text = getString(R.string.read_mode, min.toString())

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
            }
            runOnUiThread {
                binding.timerView.text = getString(R.string.read_mode, min.toString())
            }
        }
    }

    private fun stopTimer() {
        timerTask?.cancel()
    }

    private fun showPopup() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.alert_popup, null)

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("그만 들으시겠어요?")
            .setPositiveButton("확인") {dialog, which ->
                // 책 아이디, 독서시간 가지고 감상문 작성하러가기
                val bookId = intent.getIntExtra("bookId", 0)
                val libraryId = intent.getIntExtra("libraryId", 0)
                val commentIntent = Intent(this, CommentActivity::class.java)
                commentIntent.putExtra("time", min+1)
                commentIntent.putExtra("bookId", bookId)
                commentIntent.putExtra("libraryId", libraryId)
                startActivity(commentIntent)
                finish()
            }
            .setNeutralButton("취소", null)
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }
}