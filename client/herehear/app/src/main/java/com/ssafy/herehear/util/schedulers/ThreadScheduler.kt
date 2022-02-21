package com.ssafy.herehear.util.schedulers

import io.reactivex.Scheduler

typealias ExecutionBlock = () -> Unit

interface ThreadScheduler {
    fun asRxScheduler(): Scheduler
    fun runOnThread(runnable: ExecutionBlock)
}