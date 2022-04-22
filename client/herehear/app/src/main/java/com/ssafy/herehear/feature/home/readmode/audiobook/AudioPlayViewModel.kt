package com.ssafy.herehear.feature.home.readmode.audiobook

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.data.local.entity.Audio
import com.ssafy.herehear.data.repository.AudioPlayRepository
import com.ssafy.herehear.util.schedulers.UIThreadScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import java.io.File
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class AudioPlayViewModel @Inject constructor(
    private val repository: AudioPlayRepository,
    private val uiThreadScheduler: UIThreadScheduler
) : ViewModel() {

    var bookId: Int? = null
    var downloadId: Long = 0L
    var realPath: String? = null


    private lateinit var downloadManager: DownloadManager

    private val _audioList = MutableLiveData<List<Audio>>()
    val audioList: LiveData<List<Audio>> = _audioList

    private val _downloadStatus = MutableLiveData<Boolean>()
    val downloadStatus: LiveData<Boolean> = _downloadStatus

    private val _audioMax = MutableLiveData<Int>()
    val audioMax: LiveData<Int> = _audioMax

    private val _audioStarted = MutableLiveData<Boolean>()
    val audioStarted: LiveData<Boolean> = _audioStarted

    private val _selectedAudioList = MutableLiveData<MutableList<Audio>>()
    val selectedAudioList: LiveData<MutableList<Audio>> = _selectedAudioList

    private val compositeDisposable = CompositeDisposable()

    var mediaPlayer: MediaPlayer
    private var position = 0
    var audio: Audio? = null

    init {
        mediaPlayer = MediaPlayer()
    }

    fun saveBookId(bookId: Int?) {
        this.bookId = bookId
    }

    fun subscribeAudio() {
        compositeDisposable += repository.getAudioWithChanges(bookId!!)
            .observeOn(uiThreadScheduler.asRxScheduler())
            .subscribeBy(onNext = { audio ->
                _audioList.postValue(audio)
            })
    }

    private fun insertAudio(audio: Audio) {
        repository.insertAudio(audio)
    }

    fun selectAudio(audio: Audio) {
        val audioList = _selectedAudioList.value ?: mutableListOf<Audio>()
        audioList.add(audio)
        _selectedAudioList.postValue(audioList)
    }

    fun deleteAudio(audio: Audio) {
        val audioList = _selectedAudioList.value
        if (audioList != null) {
            if (audio in audioList) {
                audioList.remove(audio)
                _selectedAudioList.postValue(audioList!!)
            }
        }
    }

    fun downloadAudio() {
        Log.d("test", "downloadAudio 함수시작")
        val application = CustomApplication.instance
        val context = application!!.applicationContext
        val title = newFileName()
        val file = File(context.getExternalFilesDir(
            Environment.DIRECTORY_MUSIC), title)
        val userId = CustomApplication.prefs.getString("userId", "")
        val url = "http://10.0.2.2:8000/apis/download/${userId}/"
        val intentFilter = IntentFilter()
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(mReceiver, intentFilter)
        val downloadRequest = DownloadManager.Request(Uri.parse(url))
            .setTitle(title)
            .setDescription("오디오파일을 다운로드 중입니다.")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationUri(Uri.fromFile(file))
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
        downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        // 동시에 여러 개의 다운로드를 할 거라면 해시테이블로 downloadId : audioId을 만들어서 저장해놓으면 될 것 같다.
        downloadId = downloadManager.enqueue(downloadRequest)
    }

    private val mReceiver = object: BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("test", "mReceiver onReceive 실행")
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(intent!!.action)) {
                if (downloadId == id) {
                    val query: DownloadManager.Query = DownloadManager.Query()
                    query.setFilterById(id)
                    var cursor = downloadManager.query(query)
                    if (!cursor.moveToFirst()) {
                        return
                    }

                    var columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    var status = cursor.getInt(columnIndex)
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {

                        val title = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_TITLE))
                        val file = File(context!!.getExternalFilesDir(
                            Environment.DIRECTORY_MUSIC), title)
                        Log.d("test", "다운로드 성공")
                        Toast.makeText(context, "Download succeeded", Toast.LENGTH_SHORT).show()
                        // file 데이터를 리턴해주어야 한다.
//                        val application = CustomApplication.instance
//                        val fileName = newFileName()
//                        val file = File(application!!.applicationContext.getExternalFilesDir(
//                            Environment.DIRECTORY_MUSIC), fileName)
                        val audio = Audio(title=title, book_id = bookId!!, directory = file.path)
                        insertAudio(audio)
                        _downloadStatus.postValue(true)
                    } else if (status == DownloadManager.STATUS_FAILED) {
                        Log.d("test", "다운로드 실패")
                        _downloadStatus.postValue(false)
                    }
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(intent.action)) {
                Toast.makeText(context, "Notification clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun newFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())

        return "$filename.wav"
    }

    fun pauseAudio() {
        if (mediaPlayer.isPlaying) {
            Log.d("test", "pauseAudio!")
            position = mediaPlayer.currentPosition
            mediaPlayer.pause()
            _audioStarted.postValue(false)
        }
    }

    fun resumeAudio() {
        Log.d("test", "resumeAudio!")
        mediaPlayer.seekTo(position)
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            _audioStarted.postValue(true)
        }
    }

    fun startAudio(audio: Audio) {
        if (audio != this.audio) {
            this.audio = audio
            mediaPlayer.setDataSource(audio.directory)
            mediaPlayer.prepare()
            position = 0
            _audioMax.postValue(mediaPlayer.duration)
        }
        resumeAudio()
    }

    fun changeDuration(progress: Int) {
        position = progress
        resumeAudio()
    }

    fun finishMediaPlayer() {
        mediaPlayer.release()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        mediaPlayer.release()
    }
}