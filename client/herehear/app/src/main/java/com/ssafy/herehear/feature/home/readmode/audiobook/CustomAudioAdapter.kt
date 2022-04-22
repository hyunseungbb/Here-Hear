package com.ssafy.herehear.feature.home.readmode.audiobook

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.herehear.R
import com.ssafy.herehear.data.local.entity.Audio
import com.ssafy.herehear.databinding.AudioRecyclerBinding

class CustomAudioAdapter(private val listener: CustomAudioAdapter.OnItemClicked): RecyclerView.Adapter<CustomAudioAdapter.Holder>() {

    interface OnItemClicked {
        fun onMediaButtonClicked(binding: AudioRecyclerBinding, audio: Audio)
        fun onCheckBoxClicked(audio: Audio, isChecked: Boolean)
    }

    var listData = listOf<Audio>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAudioAdapter.Holder {
        val binding = AudioRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, parent)
    }

    override fun onBindViewHolder(holder: CustomAudioAdapter.Holder, position: Int) {
        val audio = listData.get(position)
        holder.setText(audio)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(audioList: List<Audio>) {
        this.listData = audioList
        notifyDataSetChanged()
    }

    inner class Holder(val binding: AudioRecyclerBinding, val parent: ViewGroup): RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun setText(audio: Audio) {

            binding.audioTitleText.text = audio.title
            binding.audioBookText.text = audio.book_id.toString()

            binding.audioClickFrame.setOnClickListener {
                listener.onMediaButtonClicked(binding, audio)
            }

            binding.audioCheckBox.setOnClickListener {
                if (it is CheckBox) {
                    val checked = it.isChecked
                    listener.onCheckBoxClicked(audio, checked)
                }
            }
        }
    }
}