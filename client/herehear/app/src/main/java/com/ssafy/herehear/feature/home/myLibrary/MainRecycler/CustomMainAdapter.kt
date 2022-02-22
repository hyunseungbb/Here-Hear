package com.ssafy.herehear.feature.home.myLibrary.MainRecycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.databinding.HomeBookRecyclerBinding
import com.ssafy.herehear.homeFragment


import com.ssafy.herehear.data.network.response.GetMyLibraryResponseItem

class CustomMainAdapter: RecyclerView.Adapter<CustomMainAdapter.Holder>() {

    var listData = mutableListOf<Library>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = HomeBookRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d("test", "onBindViewHolder! ${position}")
        val mainBook = listData.get(position)
        holder.setImage(mainBook)
        holder.setClick(mainBook)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    inner class Holder(val binding: HomeBookRecyclerBinding, val parent: ViewGroup): RecyclerView.ViewHolder(binding.root) {
//        init {
//            binding.root.setOnClickListener {
//                homeFragment.goDetailFragment()
//            }
//        }

        // 화면에 데이터 세팅
        fun setImage(mainBook: Library) {
            // url로 어떻게 이미지 세팅하는 지 내일 알아보기
            Glide.with(binding.homeBookImageView).load(mainBook.img_url)
                .into(binding.homeBookImageView)
        }

        fun setClick(mainBook: Library) {
            binding.root.setOnClickListener {
                HereHear.setBookStars(mainBook.stars)
                HereHear.setBookStatus(mainBook.read_status)
//                libraryMainFragment.setFragmentResult("request", bundleOf("valueKey" to mainBook.book_id))
                homeFragment.goDetailFragment(mainBook.book_id, mainBook.id)
            }
        }
    }

}
