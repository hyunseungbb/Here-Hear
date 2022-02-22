package com.ssafy.herehear.feature.home.myLibrary

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.repository.LibraryMainRepository
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import io.reactivex.Flowable
import io.reactivex.rxkotlin.subscribeBy

//import javax.inject.Inject

//@HiltViewModel
class LibraryMainViewModel constructor(
    private val repository: LibraryMainRepository,
    private val uiThreadScheduler: ThreadScheduler
): ViewModel() {

    val library: Flowable<List<Library>> = repository.getLibraryWithChanges()
        .observeOn(uiThreadScheduler.asRxScheduler())

    @SuppressLint("CheckResult")
    fun getMyLibrary() {
        Log.d("test", "확인1")
        repository.fetch()
            .subscribeBy(onSuccess = {
                Log.d("test", "fetch 성공")
            })
    }
}