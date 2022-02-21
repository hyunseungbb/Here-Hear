package com.ssafy.herehear.util.schedulers

import android.os.Handler
import android.os.Looper
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers


class UIThreadScheduler : ThreadScheduler {
    override fun asRxScheduler(): Scheduler = AndroidSchedulers.mainThread()

    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun runOnThread(runnable: ExecutionBlock) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable()
        } else {
            handler.post(runnable)
        }
    }
}