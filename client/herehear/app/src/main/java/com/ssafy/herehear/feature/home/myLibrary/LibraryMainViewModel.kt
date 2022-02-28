package com.ssafy.herehear.feature.home.myLibrary

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.repository.LibraryMainRepository
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

//import javax.inject.Inject

@HiltViewModel
class LibraryMainViewModel @Inject constructor(
    private val repository: LibraryMainRepository,
    private val uiThreadScheduler: UIThreadScheduler
): ViewModel() {

    val library: Flowable<List<Library>> = repository.getLibraryWithChanges()
        .observeOn(uiThreadScheduler.asRxScheduler())

    @SuppressLint("CheckResult")
    fun getMyLibrary() {
        Log.d("test", "확인1")
        if (!repository.isTaskRunning()) {
            repository.fetch()
                .subscribeBy(onSuccess = {
                    Log.d("test", "fetch 성공")
                })
        }
    }
}