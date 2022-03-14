package com.ssafy.herehear.feature.search.adapater

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ssafy.herehear.data.network.response.SearchResponse
import com.ssafy.herehear.databinding.SearchRecyclerBinding
import com.ssafy.herehear.feature.search.mainSearchFragment
import com.ssafy.herehear.data.network.response.SearchResponseItem
import com.ssafy.herehear.searchFragment
import okhttp3.internal.notify

class SearchAdapter: RecyclerView.Adapter<SearchAdapter.ViewHolder>(){
    var listData = mutableListOf<SearchResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = SearchRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, parent)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val bookData = listData.get(position)
        viewHolder.setImage(bookData)
        viewHolder.setClick(bookData)

    }

    override fun getItemCount():Int {
      return listData.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateData(bookList: MutableList<SearchResponseItem>) {
        this.listData = bookList
        notifyDataSetChanged()
    }


    inner class ViewHolder(val binding: SearchRecyclerBinding, val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root) {

        fun setImage(bookData: SearchResponseItem) {
            // url로 어떻게 이미지 세팅하는 지 내일 알아보기
            var bookUrl = bookData.img_url
            Glide.with(binding.searchBookImage).load(bookUrl)
                .into(binding.searchBookImage)
        }

        fun setClick(bookData: SearchResponseItem) {
            binding.searchBookImage.setOnClickListener {
                mainSearchFragment.setFragmentResult("request", bundleOf("valueKey" to bookData.id))
                mainSearchFragment.func(bookData.id)
                searchFragment.goInfoFragment(bookData.id, bookData.img_url, bookData.title)
            }
        }

    }
}
