package com.ssafy.herehear.feature.home.readmode.audiobook

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.CustomApplication
import java.io.File

class AudioPlayViewModel: ViewModel() {

    var bookId: Int? = null
    var downloadId: Long = 0L
    private lateinit var downloadManager: DownloadManager

    fun saveBookId(bookId: Int?) {
        this.bookId = bookId
    }

    fun downloadAudio() {
        val application = CustomApplication.instance
        val context = application!!.applicationContext
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_MUSIC), "myAudio.wav")
        val url = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(mReceiver, intentFilter)
        val downloadRequest = DownloadManager.Request(Uri.parse(url))
            .setTitle("오디오북 다운로드 중")
            .setDescription("오디오파일을 다운로드 중입니다.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(downloadRequest)
    }

    private val mReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent!!.action)) {
                if (downloadId == id) {
                    Log.d("test", "!!!!!!!!!")
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    var cursor = downloadManager.query(query)
                    if (!cursor.moveToFirst()) {
                        return
                    }

                    var columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    var status = cursor.getInt(columnIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        Log.d("test", "다운로드 성공")
                        Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()

                        // file 데이터를 리턴해주어야 한다.
                        val application = CustomApplication.instance
                        val file = File(application!!.applicationContext.getExternalFilesDir(
                            Environment.DIRECTORY_MUSIC), "myAudio.wav")

                        val mediaPlayer = MediaPlayer()
                        mediaPlayer.setDataSource(file.path)
                        mediaPlayer.prepare()
                        mediaPlayer.seekTo(0)
                        mediaPlayer.start()
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Log.d("test", "다운로드 실패")
                        Toast.makeText(context, "Download failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.action)) {
                Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            }
        }

    }

}