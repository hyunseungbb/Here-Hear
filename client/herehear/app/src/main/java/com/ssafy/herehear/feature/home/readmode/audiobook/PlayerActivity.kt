package com.ssafy.herehear.feature.home.readmode.audiobook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    val audioPlayViewModel: AudioPlayViewModel by viewModels()
    var bookId: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        bookId = intent.getIntExtra("bookId", -1)
        Log.d("test", "현재 bookId는 ${bookId}")
        audioPlayViewModel.saveBookId(bookId)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.playerFrameLayout, AudioPlayFragment())
                .commit()
        }
    }

}