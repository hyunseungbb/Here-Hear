package com.ssafy.herehear.worker

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
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ssafy.herehear.HereHear
import kotlinx.coroutines.delay
import java.io.File
import kotlin.properties.Delegates

class DownloadWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 1L
    }
    private var downloadId: Long = -1L
    private lateinit var downloadManager: DownloadManager
    override suspend fun doWork(): Result {
        // 다운로드해보자.
        val firstUpdate = workDataOf(Progress to 0)
        val lastUpdate = workDataOf(Progress to 100)
        setProgress(firstUpdate)
        delay(delayDuration)
        setProgress(lastUpdate)
        val userId = inputData.getString("userId")
        val file = File(applicationContext.getExternalFilesDir(
            Environment.DIRECTORY_MUSIC), "myAudio.wav")
        val path = file.path
        Log.d("test", "${path}")
//        val url = "http://10.0.2.2:8000/media/tmp/file_example_MP3_700KB.mp3"
//        Log.d("test", "${url}")
        val url = "https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_700KB.mp3"
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        applicationContext.registerReceiver(onDownloadComplete, intentFilter)
        val downloadRequest = DownloadManager.Request(Uri.parse(url))
            .setTitle("오디오북 다운로드 중")
            .setDescription("오디오파일을 다운로드 중입니다.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(downloadRequest)

        return Result.success()
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
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
                        val file = File(applicationContext.getExternalFilesDir(
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