package com.ssafy.herehear.feature.home.myLibrary

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem
import com.ssafy.herehear.data.repository.LibraryDetailRepository
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

@HiltViewModel
class LibraryDetailViewModel @Inject constructor(
    private val repository: LibraryDetailRepository,
    private val uiThreadScheduler: UIThreadScheduler
): ViewModel() {

    @SuppressLint("CheckResult")
    fun getComments(bookId: Int) {
        val url = "comment/${bookId}"
        repository.getComments(url)
            .subscribeBy(onSuccess = { data ->
            })
    }

}