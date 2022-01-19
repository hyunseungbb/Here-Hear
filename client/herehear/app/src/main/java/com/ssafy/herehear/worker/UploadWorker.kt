package com.ssafy.herehear.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val realPath = inputData.getString("realPath")
        val userId = inputData.getString("userId")
        Log.d("test", "${realPath}, ${userId}")
        return Result.success()
    }
}