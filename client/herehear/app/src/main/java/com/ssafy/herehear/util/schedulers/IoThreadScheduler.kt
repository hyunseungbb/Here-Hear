package com.ssafy.herehear.util.schedulers


import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger


class IoThreadScheduler : ThreadScheduler {

    private val threadPoolExecutor: Executor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE.toLong(),
        TimeUnit.SECONDS, POOL_WORK_QUEUE, THREAD_FACTORY)

    companion object {
        const val CORE_POOL_SIZE = 5
        const val MAXIMUM_POOL_SIZE = 128
        const val KEEP_ALIVE = 1

        @JvmField
        val THREAD_FACTORY = object : ThreadFactory {
            private val mCount = AtomicInteger(1)

            override fun newThread(r: Runnable): Thread {
                return Thread(r, "IoThread#" + mCount.getAndIncrement())
            }
        }

        @JvmField
        val POOL_WORK_QUEUE = LinkedBlockingQueue<Runnable>()
    }

    override fun asRxScheduler(): io.reactivex.Scheduler = Schedulers.from(threadPoolExecutor)

    override fun runOnThread(runnable: ExecutionBlock) {
        threadPoolExecutor.execute(runnable)
    }
}