package com.ssafy.herehear.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ssafy.herehear.model.network.RetrofitClientAI
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
                Result.success()
            } else {
                Result.failure()
            }
        }
    }
}