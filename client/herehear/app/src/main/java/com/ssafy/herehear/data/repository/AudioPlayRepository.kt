package com.ssafy.herehear.data.repository

import android.util.Log
import com.ssafy.herehear.data.local.dao.AudioDao
import com.ssafy.herehear.data.local.entity.Audio
import com.ssafy.herehear.util.schedulers.IoThreadScheduler
import com.ssafy.herehear.util.schedulers.NetworkThreadScheduler
import io.reactivex.Flowable
import java.util.concurrent.atomic.AtomicBoolean

class AudioPlayRepository(
    val audioDao: AudioDao,
    val networkThreadScheduler: NetworkThreadScheduler,
    val ioThreadScheduler: IoThreadScheduler
) {

    private val isTaskRunning = AtomicBoolean(false)
    fun isTaskRunning(): Boolean = isTaskRunning.get()

    fun getAudioWithChanges(bookId: Int): Flowable<List<Audio>> {
        Log.d("test", "가져올 bookId는 ${bookId}")
        return audioDao.getAudioWithChanges(bookId)
    }

    fun insertAudio(audio: Audio) {
        audioDao.insertAudio(audio)
    }

    fun deleteAudio(audio: Audio) {
        audioDao.deleteAudio(audio)
    }
}