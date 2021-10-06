package com.ssafy.herehear.feature.search.adapater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.herehear.databinding.SearchDetailRecyclerBinding
import com.ssafy.herehear.databinding.SearchRecyclerBinding
import com.ssafy.herehear.model.network.response.SearchResponseItem
import com.ssafy.herehear.searchFragment

class SearchDetailAdapter: RecyclerView.Adapter<SearchDetailAdapter.ViewHolder>() {
    var listData = mutableListOf<SearchResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = SearchDetailRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val bookData = listData.get(position)

    }

    override fun getItemCount():Int {
        return listData.size
    }

    inner class ViewHolder(val binding: SearchDetailRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun setImage(bookData: SearchResponseItem) {
//            // url로 어떻게 이미지 세팅하는 지 내일 알아보기
//            var bookUrl = bookData.img_url
//            Glide.with(binding.searchBookImage).load(bookUrl)
//                .into(binding.searchBookImage)
//        }
//
//        fun setText()

    }
}