package com.ssafy.herehear.data.repository

import android.annotation.SuppressLint
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.response.AllCommentsResponse
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.atomic.AtomicBoolean

class LibraryDetailRepository(
    val libraryDao: LibraryDao,
    val networkThreadScheduler: NetworkThreadScheduler,
    val ioThreadScheduler: IoThreadScheduler
) {

    private val isTaskRunning = AtomicBoolean(false)
    fun isTaskRunning(): Boolean = isTaskRunning.get()

    @SuppressLint("CheckResult")
    fun getComments(url: String): Single<MutableList<AllCommentsResponseItem>> = Single.create { emitter ->
        RetrofitClient.api.getAllComments(url)
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { comments ->
                val data = mutableListOf<AllCommentsResponseItem>()
                for (item in comments) {
                    data.add(item)
                }
                emitter.onSuccess(data)
            }, onError =  {throwable ->
                emitter.onError(throwable)
            })
    }

    fun getLibrary(bookId: Int): Library {
        return libraryDao.getDetail(bookId)
    }

}