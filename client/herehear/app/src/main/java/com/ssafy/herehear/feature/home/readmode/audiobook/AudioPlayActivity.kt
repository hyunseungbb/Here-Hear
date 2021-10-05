package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.app.DownloadManager
import android.content.*
import android.media.MediaPlayer
import android.net.Uri
import android.net.Uri.withAppendedPath
import android.os.*
import android.util.Log
import android.widget.Toast
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.TextView
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityAudioPlayBinding
import com.ssafy.herehear.feature.home.readmode.CommentActivity
import java.io.File
import java.util.*
import kotlin.concurrent.timer


class AudioPlayActivity : AppCompatActivity() {
    val PERM_STORAGE = 99
    private var downloadId: Long = -1L
    private lateinit var downloadManager: DownloadManager
    private lateinit var binding: ActivityAudioPlayBinding
    private lateinit var file: File
    private lateinit var mediaPlayer: MediaPlayer
    private var position = 0
    val handler = Handler(Looper.getMainLooper()) {
        changeImage()
        true
    }
    private var sec = 0
    private var min = 0
    private var timerTask: Timer? = null

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent.action)) {
                if (downloadId == id) {
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    var cursor = downloadManager.query(query)
                    if (!cursor.moveToFirst()) {
                        return
                    }

                    var columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    var status = cursor.getInt(columnIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.action)) {
                Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        downloadManager = HereHear.context().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

//        val intentFilter = IntentFilter()
//        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//        intentFilter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
//        registerReceiver(onDownloadComplete, intentFilter)
//
//        downloadImage("http://10.0.2.2:8000/apis/download/")
//        downloadAudio()
        downloadAudio()
        binding.audioPlayButton.isSelected = true
        binding.audioView.text = getString(R.string.read_mode, sec.toString())
        binding.audioImageView.setImageResource(R.drawable.read0)

        binding.audioPlayButton.setOnClickListener {
            if (binding.audioPlayButton.isSelected) {
                    startTimer()
                    resumeAudio()
            } else {
                pauseAudio()
                stopTimer()
            }
            binding.audioPlayButton.isSelected = !binding.audioPlayButton.isSelected
        }

        binding.audioStopButton.setOnClickListener {
            // 일단 정지 후
            pauseAudio()
            stopTimer()
            binding.audioPlayButton.isSelected = !binding.audioPlayButton.isSelected
            // 팝업창 띄워서 진짜 책 다읽었냐고 물어보기
            showPopup()
            // 1. 다 읽었으면 stopAudio 후 커멘트작성페이지로 넘어가기
//            stopAudio()
            // 2. 다 취소 누르면 그냥 그대로

        }
    }



//    override fun onDestroy() {
//        super.onDestroy()
//        unregisterReceiver(onDownloadComplete)
//    }

    private fun downloadImage(url: String) {

        file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "latest_audio.mp3")

        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Downloading a video")
            .setDescription("Downloading Dev Summit")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        downloadId = downloadManager.enqueue(request)
        Log.d("test", "path : " + file.path)

    }

    fun downloadAudio() {
        val filepath = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "latest_audio.mp3").path
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource("http://192.168.35.188:8000/apis/download/")
        mediaPlayer.prepare()
        mediaPlayer.isLooping = true
    }

    fun pauseAudio() {
        if (mediaPlayer != null) {
            position = mediaPlayer.currentPosition
            mediaPlayer.pause()
        }
    }

    fun resumeAudio() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying) {
            mediaPlayer.seekTo(position)
            mediaPlayer.start()
        }
    }

    fun stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
            position = 0
        }
    }

    private fun changeImage() {
        var a = (0..5).random()
        when (a) {
            0 -> {
                binding.audioImageView.setImageResource(R.drawable.read0)
            }
            1 -> {
                binding.audioImageView.setImageResource(R.drawable.read1)
            }
            2 -> {
                binding.audioImageView.setImageResource(R.drawable.read2)
            }
            3 -> {
                binding.audioImageView.setImageResource(R.drawable.read3)
            }
            4 -> {
                binding.audioImageView.setImageResource(R.drawable.read4)
            }
            5 -> {
                binding.audioImageView.setImageResource(R.drawable.read5)
            }

        }
    }

    private fun startTimer() {
        timerTask = timer(period = 1000) {

            sec++
            if (sec == 60) {
                min++
                sec = 0
            }
            if (sec % 5 == 0) {
                handler.obtainMessage().sendToTarget()
            }
            // 일단은 빨리 확인하기 위해 초단위로
            runOnUiThread {
                binding.audioView.text = getString(R.string.read_mode, sec.toString())
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
                stopAudio()
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
//    private fun getStatus(id: Long): String {
//        val query: DownloadManager.Query = DownloadManager.Query()
//        query.setFilterById(id)
//        var cursor = downloadManager.query(query)
//        if (!cursor.moveToFirst()) {
//            Log.e("test", "Empty row")
//            return "Wrong downloadId"
//        }
//
//        var columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
//        var status = cursor.getInt(columnIndex)
//        var columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
//        var reason = cursor.getInt(columnReason)
//        var statusText: String
//
//        when (status) {
//            DownloadManager.STATUS_SUCCESSFUL -> statusText = "Successful"
//            DownloadManager.STATUS_FAILED -> {
//                statusText = "Failed: $reason"
//            }
//            DownloadManager.STATUS_PENDING -> statusText = "Pending"
//            DownloadManager.STATUS_RUNNING -> statusText = "Running"
//            DownloadManager.STATUS_PAUSED-> {
//                statusText = "Paused: $reason"
//            }
//            else -> statusText = "Unknown"
//        }
//
//        return statusText
//    }
}