package com.ssafy.herehear.feature.home.myLibrary

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.data.local.entity.Book
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem
import com.ssafy.herehear.data.repository.LibraryDetailRepository
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Emitter
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import javax.inject.Inject

@HiltViewModel
class LibraryDetailViewModel @Inject constructor(
    private val repository: LibraryDetailRepository,
    private val uiThreadScheduler: UIThreadScheduler
): ViewModel() {

    private val _commentList = MutableLiveData<MutableList<AllCommentsResponseItem>>()
    val commentList: LiveData<MutableList<AllCommentsResponseItem>> = _commentList

    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book> = _book

    private val _library = MutableLiveData<Library>()
    val library: LiveData<Library> = _library

    private val _readDoneState = MutableLiveData<Boolean>()
    val readDoneState: LiveData<Boolean> = _readDoneState

    private val _rateUpdateState = MutableLiveData<Float>()
    val rateUpdateState: LiveData<Float> = _rateUpdateState

    private val _goNextState = MutableLiveData<Int>()
    val goNextState: LiveData<Int> = _goNextState

    val compositeDisposable = CompositeDisposable()

    @SuppressLint("CheckResult")
    fun getComments(bookId: Int) {
        val url = "comment/${bookId}"
        compositeDisposable += repository.getComments(url)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = { data ->
                _commentList.postValue(data)
            })
    }
    // type
    // ??? ??????????????? Dao??? ???????????????...
    // 0, 1 -> ???????????? ??? ????????? ??????
    // 2. ??? ?????? ??? ???????????? ??????
    // 3. ????????? ???????????? ?????? -> UI??? ??????????????? ???.
    fun updateBookStatus(libraryId: Int, bookStatus: Int, rating: Float, type: Int) {
        compositeDisposable += repository.updateBookStatus(libraryId, bookStatus, rating)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = {
                // library ?????? ????????? ???????????? ????????? ??????. library????????? ????????? ?????? ??????..
                val library = Library(_book.value!!.id.toInt(), libraryId, _book.value!!.img_url, bookStatus, rating.toInt())
                repository.insertLibrary(library)
                when(type) {
                    2 -> _readDoneState.postValue(true)
                    3 -> _rateUpdateState.postValue(rating)
                    else -> _goNextState.postValue(type)
                }

            }, onError = {
                when(type) {
                    2 -> _readDoneState.postValue(false)
                    3 -> _rateUpdateState.postValue(-1F)
                    else -> _goNextState.postValue(2)
                }
            })
    }


    fun getDetail(bookId: Int) {
        val data = repository.getBookDetail(bookId)
        _book.postValue(data)
    }

    fun getLibrary(bookId: Int) {
        compositeDisposable += repository.getLibrary(bookId)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onNext = { library ->
                _library.postValue(library)
            })
    }

    fun updateLibrary(library: Library) {
        repository.insertLibrary(library)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}