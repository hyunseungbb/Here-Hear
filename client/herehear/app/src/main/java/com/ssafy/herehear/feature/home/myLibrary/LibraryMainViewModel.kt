package com.ssafy.herehear.feature.home.myLibrary

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.repository.LibraryMainRepository
import com.ssafy.herehear.util.schedulers.ThreadScheduler
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


@HiltViewModel
class LibraryMainViewModel @Inject constructor(
    private val repository: LibraryMainRepository,
    private val uiThreadScheduler: UIThreadScheduler,
): ViewModel() {

    private val _libraryList = MutableLiveData<List<Library>>()
    val libraryList: LiveData<List<Library>> = _libraryList
    private val compositeDisposable = CompositeDisposable()

    init {
        subscribeLibrary()
        getMyLibrary()
    }

    @SuppressLint("CheckResult")
    fun getMyLibrary() {
        if (!repository.isTaskRunning()) {
                repository.fetch()
        }
    }

    fun subscribeLibrary() {
        compositeDisposable += repository.getLibraryWithChanges()
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onNext = { library ->
                _libraryList.postValue(library)
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}