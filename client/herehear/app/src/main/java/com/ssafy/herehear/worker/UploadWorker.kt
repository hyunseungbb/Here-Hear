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
import androidx.work.Data
import androidx.work.WorkerParameters
import com.ssafy.herehear.data.network.RetrofitClientAI
import com.ssafy.herehear.util.FormDataUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class UploadWorker(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val realPath = inputData.getString("realPath")
            val userId = inputData.getString("userId")

            val file = File(realPath)
            var fileBody = FormDataUtil.getImageBody("imgs", file)
            val url = "ocr_tts/${userId}/"
            val res = RetrofitClientAI.api.downloadAudio(url, fileBody)
            if (res.isSuccessful) {
                Log.d("test", "서버에서 오디오로 변환 완료")
                Result.success()
            } else {
                Result.failure()
            }
        }
    }

}