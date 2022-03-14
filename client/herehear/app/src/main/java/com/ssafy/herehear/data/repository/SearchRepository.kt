package com.ssafy.herehear.data.repository

import android.annotation.SuppressLint
import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.data.local.dao.BookDao
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.entity.Book
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.RetrofitClientRecommend
import com.ssafy.herehear.data.network.response.AllCommentsResponse
import com.ssafy.herehear.data.network.response.SearchDetailResponse
import com.ssafy.herehear.data.network.response.SearchResponse
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.atomic.AtomicBoolean

class SearchRepository(
    val libraryDao: LibraryDao,
    val bookDao: BookDao,
    val networkThreadScheduler: NetworkThreadScheduler,
    val ioThreadScheduler: IoThreadScheduler
) {
    private val isTaskRunning = AtomicBoolean(false)
    fun isTaskRunning(): Boolean = isTaskRunning.get()

    @SuppressLint("CheckResult")
    fun fetchRecommend(): Single<SearchResponse> {
        val username = CustomApplication.prefs.getString("userId", "")
        return Single.create { emitter ->
            RetrofitClientRecommend.api.getRecommend(username)
                .doOnSubscribe { isTaskRunning.set(true) }
                .subscribeOn(networkThreadScheduler.asRxScheduler())
                .observeOn(ioThreadScheduler.asRxScheduler())
                .doFinally { isTaskRunning.set(false) }
                .subscribeBy(onSuccess = { searchResponse ->
                    emitter.onSuccess(searchResponse)
                }, onError = { throwable ->
                    emitter.onError(throwable)
                })
        }
    }

    @SuppressLint("CheckResult")
    fun fetchSearch(text: String): Single<SearchResponse> = Single.create { emitter ->
        RetrofitClient.api.getSearch("title", text)
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { searchResponse ->
                emitter.onSuccess(searchResponse)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }

    @SuppressLint("CheckResult")
    fun fetchSearchDetail(url: String) : Single<SearchDetailResponse> = Single.create { emitter ->
        RetrofitClient.api.getSearchDetail(url)
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { res ->
                val book = Book(res.id, res.description, res.img_url, res.title)
                val bookList = mutableListOf(book)
                bookDao.insertBook(bookList)
                emitter.onSuccess(res)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }

    @SuppressLint("CheckResult")
    fun fetchComments(url: String): Single<AllCommentsResponse> = Single.create { emitter ->
        RetrofitClient.api.getAllComments(url)
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { comments ->
                emitter.onSuccess(comments)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }

    @SuppressLint("CheckResult")
    fun insertLibrary(url: String): Single<Library> = Single.create { emitter ->
        RetrofitClient.api.registerBook(url)
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { library ->
                libraryDao.insertLibrarys(listOf(library))
                emitter.onSuccess(library)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }

    @SuppressLint("CheckResult")
    fun deleteLibrary(url: String): Single<Unit> = Single.create { emitter ->
        RetrofitClient.api.deleteBook(url)
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = {
                emitter.onSuccess(Unit)
            }, onError = { throwable ->
                emitter.onError(throwable)
            })
    }

    fun getLibraryItem(bookId: Int): Library? = libraryDao.getDetail(bookId)

    fun deleteLibraryFromLocal(library: Library) = libraryDao.deleteLibrary(library)

    fun addLibraryLocal(library: Library) = libraryDao.insertLibrarys(listOf(library))
}