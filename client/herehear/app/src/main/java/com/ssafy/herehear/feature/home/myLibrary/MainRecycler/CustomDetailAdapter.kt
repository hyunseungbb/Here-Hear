package com.ssafy.herehear.feature.home.myLibrary.MainRecycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.herehear.databinding.DetailBookRecyclerBinding
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem

class CustomDetailAdapter: RecyclerView.Adapter<CustomDetailAdapter.Holder>() {

    var listData = mutableListOf<AllCommentsResponseItem>()
//    var listData = AllCommentsResponse()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = DetailBookRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = listData.get(position)
        holder.setText(comment)
//        holder.setClick(comment)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(val binding: DetailBookRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.root.setOnClickListener {
//                homeFragment.goDetailFragment()
//            }
//        }

        // 화면에 데이터 세팅
        fun setText(comment: AllCommentsResponseItem) {
            binding.commentDateTextView.text = comment.date.split('T')[0]
            binding.commentNameTextView.text = comment.username
            binding.commentTextView.text = comment.content
        }

    }
}