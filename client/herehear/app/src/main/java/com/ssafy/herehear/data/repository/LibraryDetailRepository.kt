package com.ssafy.herehear.data.repository

import android.annotation.SuppressLint
import com.ssafy.herehear.data.local.dao.BookDao
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.entity.Book
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.response.AllCommentsResponse
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem
import com.ssafy.herehear.data.network.response.UpdateBookStatusRequest
import com.ssafy.herehear.data.network.response.UpdateBookStatusResponse
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import retrofit2.Retrofit
import java.util.concurrent.atomic.AtomicBoolean

class LibraryDetailRepository(
    val libraryDao: LibraryDao,
    val bookDao: BookDao,
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

    fun getBookDetail(bookId: Int): Book {
        return bookDao.getDetail(bookId)
    }

    fun getLibrary(bookId: Int): Flowable<Library> {

        return libraryDao.getDetailWithChanges(bookId)
    }

    fun insertLibrary(library: Library) {
        libraryDao.insertLibrarys(listOf(library))
    }

    @SuppressLint("CheckResult")
    fun updateBookStatus(libraryId: Int, bookStatus: Int, rating: Float) : Single<UpdateBookStatusResponse> = Single.create { emitter ->
        val data = UpdateBookStatusRequest(libraryId, bookStatus, rating.toInt())
        RetrofitClient.api.updateBookStatus(data)
            .doOnSubscribe{ isTaskRunning.set(true)}
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally{ isTaskRunning.set(false)}
            .subscribeBy(onSuccess = { res ->
                emitter.onSuccess(res)
            }, onError = { throwable -> emitter.onError(throwable) })
    }


}