package com.ssafy.herehear.feature.search.adapater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.herehear.databinding.SearchDetailRecyclerBinding
import com.ssafy.herehear.data.network.response.AllCommentsResponseItem

class SearchDetailAdapter: RecyclerView.Adapter<SearchDetailAdapter.ViewHolder>() {
    var listData = mutableListOf<AllCommentsResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = SearchDetailRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val bookData = listData.get(position)
        viewHolder.setComment(bookData)

    }

    override fun getItemCount():Int {
        return listData.size
    }

    inner class ViewHolder(val binding: SearchDetailRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setComment(bookData: AllCommentsResponseItem){
            binding.recyclerDate.text = bookData.date.split('T')[0]
            binding.recyclerUserName.text = bookData.username
            binding.recyclerComment.text = bookData.content
        }

    }
}