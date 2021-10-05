package com.ssafy.herehear.feature.calender.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.herehear.databinding.CalendarCommentRecyclerBinding
import com.ssafy.herehear.model.network.response.AllCommentsResponseItem

class CustomCalendarAdpater: RecyclerView.Adapter<CustomCalendarAdpater.Holder>() {

    var listData = mutableListOf<AllCommentsResponseItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomCalendarAdpater.Holder {
        val binding = CalendarCommentRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: CustomCalendarAdpater.Holder, position: Int) {
        val comment = listData.get(position)
        holder.setText(comment)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(val binding: CalendarCommentRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
        fun setText(comment: AllCommentsResponseItem) {
        }
    }
}