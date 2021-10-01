package com.ssafy.herehear.feature.home.readmode.audiobook

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.DownloadManager
import android.content.*
import android.media.MediaPlayer
import android.net.Uri
import android.net.Uri.withAppendedPath
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import android.provider.MediaStore
import com.ssafy.herehear.BaseActivity
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.databinding.ActivityAudioPlayBinding
import java.io.File


class AudioPlayActivity : AppCompatActivity() {
    val PERM_STORAGE = 99
    private var downloadId: Long = -1L
    private lateinit var downloadManager: DownloadManager
    private lateinit var binding: ActivityAudioPlayBinding
    private lateinit var file: File
    private lateinit var mediaPlayer: MediaPlayer
    private var position = 0
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


        binding.buttonTmp.setOnClickListener {
            playAudio()
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

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

    fun playAudio() {
        val filepath = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "latest_audio.mp3").path
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource("http://10.0.2.2:8000/apis/download/")
        mediaPlayer.prepare()
        mediaPlayer.start()
        Toast.makeText(this, "재생!!", Toast.LENGTH_LONG).show()
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
            // 여기서 다음으로 넘어가야됨
        }
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