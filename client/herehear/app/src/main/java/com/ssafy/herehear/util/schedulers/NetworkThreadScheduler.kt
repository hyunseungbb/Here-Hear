package com.ssafy.herehear.util.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Executors

class NetworkThreadScheduler: ThreadScheduler {

    private val executor = Executors.newFixedThreadPool(2)

    override fun asRxScheduler(): Scheduler = Schedulers.from(executor)

    override fun runOnThread(runnable: ExecutionBlock) {
        executor.execute(runnable)
    }
}