package com.ssafy.herehear.data.repository

import android.annotation.SuppressLint
import android.util.Log
import com.ssafy.herehear.data.local.dao.LibraryDao
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import java.util.concurrent.atomic.AtomicBoolean


class LibraryMainRepository(
    val libraryDao: LibraryDao,
    val networkThreadScheduler: NetworkThreadScheduler,
    val ioThreadScheduler: IoThreadScheduler
) {

    private val isTaskRunning = AtomicBoolean(false)
    fun isTaskRunning(): Boolean = isTaskRunning.get()

    @SuppressLint("CheckResult")
    fun fetch(): Single<Unit> = Single.create { emitter ->
        Log.d("test", "확인2")
        RetrofitClient.api.getMyLibrary()
            .doOnSubscribe { isTaskRunning.set(true) }
            .subscribeOn(networkThreadScheduler.asRxScheduler())
            .observeOn(ioThreadScheduler.asRxScheduler())
            .doFinally { isTaskRunning.set(false) }
            .subscribeBy(onSuccess = { GetMyLibraryResponse ->
                Log.d("test" ,"GetMyLibraryResponse : ${GetMyLibraryResponse}")
                // 로컬 데이터베이스에 저장
                // 이때 중복없이 업데이트만 하는 방법?
                GetMyLibraryResponse.mapIndexed { index, getMyLibraryResponseItem ->
                    with(getMyLibraryResponseItem) {
                        Library(book_id, id, img_url, read_status, stars)
                    }
                }.let { libraryList ->
                    libraryDao.insertLibrarys(libraryList)
                }
                emitter.onSuccess(Unit)
            }, onError = { throwable ->
                Log.d("test", "에러남")
                emitter.onError(throwable)
            })
    }


    fun getLibraryWithChanges(): Flowable<List<Library>> {
        return libraryDao.getLibraryWithChanges()
    }
}