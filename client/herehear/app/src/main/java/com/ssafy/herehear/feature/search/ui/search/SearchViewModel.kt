package com.ssafy.herehear.feature.search.ui.search

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem
import com.ssafy.herehear.data.network.response.SearchDetailResponse
import com.ssafy.herehear.data.network.response.SearchResponseItem
import com.ssafy.herehear.data.repository.SearchRepository
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val uiThreadScheduler: UIThreadScheduler
): ViewModel() {

    private val _bookList = MutableLiveData<MutableList<SearchResponseItem>>()
    val bookList: LiveData<MutableList<SearchResponseItem>> = _bookList

    var bookId: Int? = null
    var library: Library? = null
    private val _bookDetail = MutableLiveData<SearchDetailResponse>()
    val bookDetail: LiveData<SearchDetailResponse> = _bookDetail
    private val _comment = MutableLiveData<MutableList<AllCommentsResponseItem>>()
    val comment: LiveData<MutableList<AllCommentsResponseItem>> = _comment

    private val _isLibraryRegistered = MutableLiveData<Boolean>()
    val isLibraryRegisterd: LiveData<Boolean> = _isLibraryRegistered

    init {
        Log.d("test", "searchView 생성")
//        getRecommend()
    }

    @SuppressLint("CheckResult")
    fun getRecommend() {
        repository.fetchRecommend()
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = { searchResponse ->
                val data = mutableListOf<SearchResponseItem>()
                for (item in searchResponse) {
                    data.add(item)
                }
                _bookList.postValue(data)
            }, onError = {
                it.printStackTrace()
            })
    }

    @SuppressLint("CheckResult")
    fun getSearchResult(text: String) {
        repository.fetchSearch(text)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = { searchResponse ->
                val data = mutableListOf<SearchResponseItem>()
                for (item in searchResponse) {
                    data.add(item)
                }
                _bookList.postValue(data)
            })
    }

    // 여기서부터 detail
    @SuppressLint("CheckResult")
    fun getSearchDetail() {
        if (bookId != null) {
            val url = "books/${bookId}"
            repository.fetchSearchDetail(url)
                .observeOn(uiThreadScheduler.asRxScheduler())
                .subscribeBy(onSuccess = { searchDetailResponse ->
                    _bookDetail.postValue(searchDetailResponse)
                })
        }
    }

    @SuppressLint("CheckResult")
    fun getComments() {
        if (bookId == null) return
        val url = "comment/${bookId}"
        repository.fetchComments(url)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = { allCommentResponse ->
                val data = mutableListOf<AllCommentsResponseItem>()
                for (item in allCommentResponse) {
                    data.add(item)
                }
                _comment.postValue(data)
            })
    }

    @SuppressLint("CheckResult")
    fun addLibrary() {
        if (bookId == null) return
        if (repository.isTaskRunning()) return
        val url = "libraries/${bookId}"
        repository.insertLibrary(url)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = { library ->
                repository.addLibraryLocal(library!!)
                _isLibraryRegistered.postValue(true)
            })
    }

    @SuppressLint("CheckResult")
    fun deleteLibrary() {
        Log.d("test", "deleteLibrary 실행, ${library}")
        if (library == null) return
        if (repository.isTaskRunning()) return
        val url = "libraries/${library!!.id}"
        repository.deleteLibrary(url)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onSuccess = {
                repository.deleteLibraryFromLocal(library!!)
                this.library = null
                _isLibraryRegistered.postValue(false)
            })
    }

    @SuppressLint("CheckResult")
    fun isBookInMyLibrary() {
        if (bookId == null) return
        Log.d("test", "bookId가 확인됨")
        val library = repository.getLibraryItem(bookId!!)
        Log.d("test", "${library}")
        if (library == null) {
            _isLibraryRegistered.postValue(false)
        } else {
            this.library = library
            _isLibraryRegistered.postValue(true)
        }
//        compositeDisposable += repository.getLibraryItem(bookId!!)
//            .observeOn(uiThreadScheduler.asRxScheduler())
//            .subscribeBy(onNext = { library ->
//                Log.d("test", "library가 발행이 안되나?")
//                if (library == null) {
//                    _isLibraryRegistered.postValue(false)
//                } else {
//                    _isLibraryRegistered.postValue(true)
//                }
//            })
    }

    override fun onCleared() {
        super.onCleared()
    }
}