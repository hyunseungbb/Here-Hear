package com.ssafy.herehear.feature.home.readmode.audiobook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.herehear.R
import com.ssafy.herehear.data.local.entity.Audio
import com.ssafy.herehear.databinding.FragmentAudioPlayBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AudioPlayFragment : Fragment(), CustomAudioAdapter.OnItemClicked {

    lateinit var binding: FragmentAudioPlayBinding
    private val audioPlayViewModel: AudioPlayViewModel by activityViewModels()
    private lateinit var audioAdapter: CustomAudioAdapter

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
                    .replace(R.id.playerFrameLayout, CameraFragment())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }



    }

    override fun onItemClicked(audio: Audio) {
        TODO("Not yet implemented")
    }
}