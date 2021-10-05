package com.ssafy.herehear.feature.search.adapater

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ViewItemLayoutBinding

class SearchAdapter: RecyclerView.Adapter<ViewHolder>(){
    var listData = mutableListOf<BookUrl>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val binding = ViewItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        val url = listData.get(position)

        viewHolder.setUrl(url)

    }

    override fun getItemCount():Int {
      return listData.size
    }


}
class ViewHolder(val binding: ViewItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
    fun setUrl(bookUrl: BookUrl){
        Glide.with(binding.root).load(bookUrl.url).override(150, 200).into(binding.imageView3)
    }

}