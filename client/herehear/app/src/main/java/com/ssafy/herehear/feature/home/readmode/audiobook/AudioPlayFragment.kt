package com.ssafy.herehear.feature.home.readmode.audiobook

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.herehear.R
import com.ssafy.herehear.data.local.entity.Audio
import com.ssafy.herehear.databinding.AudioRecyclerBinding
import com.ssafy.herehear.databinding.FragmentAudioPlayBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import java.util.*
import kotlin.concurrent.thread

@AndroidEntryPoint
class AudioPlayFragment : Fragment(), CustomAudioAdapter.OnItemClicked {

    lateinit var binding: FragmentAudioPlayBinding
    private val audioPlayViewModel: AudioPlayViewModel by activityViewModels()
    private lateinit var audioAdapter: CustomAudioAdapter
    private var isInterrupted = false
    private var audioBinding: AudioRecyclerBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioPlayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioPlayViewModel.subscribeAudio()
        with(binding) {
            with(audioRecyclerView) {
                layoutManager = LinearLayoutManager(context)
                adapter = CustomAudioAdapter(this@AudioPlayFragment).also {
                    audioAdapter = it
                }
            }

            audioAddButton.setOnClickListener{
                val activity = requireActivity()
                activity.supportFragmentManager.beginTransaction()
                    .add(R.id.playerFrameLayout, CameraFragment())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }

            audioplayBackButton.setOnClickListener {
                val activity = requireActivity()
                activity.supportFragmentManager.beginTransaction()
                    .remove(this@AudioPlayFragment)
                    .commit()
            }

            mainMediaButton.setOnClickListener {
                if (it.isSelected) {
                    audioPlayViewModel.pauseAudio()
                } else {
                    audioPlayViewModel.resumeAudio()
                }
                it.isSelected = !it.isSelected
            }

            seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if(fromUser) {
                        audioPlayViewModel.changeDuration(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        audioPlayViewModel.audioList.observe(viewLifecycleOwner, Observer { audioList ->
            if (audioList == null || audioList.isEmpty()) {
                binding.emptyText.visibility = View.VISIBLE
            } else {
                audioAdapter.updateData(audioList)
                binding.emptyText.visibility = View.GONE
            }
        })

        audioPlayViewModel.audioMax.observe(viewLifecycleOwner, Observer { audioMax ->
            binding.seekBar.max = audioMax
        })

        audioPlayViewModel.audioStarted.observe(viewLifecycleOwner, Observer { audioStarted ->
            if (audioStarted) {
                startSeekBarThread()
                Log.d("test", "seekBar 스레드 시작")
            } else {
                Thread.currentThread().interrupt()
            }
        })

        audioPlayViewModel.selectedAudioList.observe(viewLifecycleOwner, Observer { audioList ->
            if (audioList == null || audioList.size == 0) return@Observer
            Log.d("test", "선택된 오디오 개수 : ${audioList.size}")
            // 휴지통 아이콘이 활성화 & 선택된 오디오 개수 업데이트
        })
    }

    private fun startSeekBarThread() {
        val activity = requireActivity()
        with(activity) {
            thread {
                while(!isInterrupted && audioPlayViewModel.mediaPlayer.isPlaying) {
                    try{
                        Thread.sleep(500)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    runOnUiThread {
                        try {
                            binding.seekBar.progress = audioPlayViewModel.mediaPlayer.currentPosition
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }
        }
    }




    override fun onMediaButtonClicked(b: AudioRecyclerBinding, audio: Audio) {
        audioPlayViewModel.startAudio(audio)
        binding.mainMediaButton.isSelected = true
        binding.audioMainTitleText.text = audio.title
        if (audioBinding != null) {
            audioBinding!!.audioFrame.setBackgroundColor(resources.getColor(R.color.transperent))
        }
        audioBinding = b
        audioBinding!!.audioFrame.setBackgroundColor(resources.getColor(R.color.selected))
    }

    override fun onCheckBoxClicked(audio: Audio, isChecked: Boolean) {
        if (isChecked) {
            Log.d("test", "오디오선택")
            audioPlayViewModel.selectAudio(audio)
        } else {
            Log.d("test", "오디오선택취소")
            audioPlayViewModel.deleteAudio(audio)
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        isInterrupted = true
        Thread.currentThread().interrupt()
        activity?.finish()
    }
}