package com.ssafy.herehear.feature.home.myLibrary.MainRecycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.HomeBookRecyclerBinding
import com.ssafy.herehear.feature.home.libraryMainFragment
import com.ssafy.herehear.feature.home.myLibrary.LibraryDetailFragment
import com.ssafy.herehear.homeFragment


import com.ssafy.herehear.model.data.MainBook
import com.ssafy.herehear.model.network.response.GetMyLibraryResponseItem
import com.ssafy.herehear.util.GlideApp

class CustomMainAdapter: RecyclerView.Adapter<CustomMainAdapter.Holder>() {

    var listData = mutableListOf<GetMyLibraryResponseItem>()

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
        fun setImage(mainBook: GetMyLibraryResponseItem) {
            Log.d("test", "dfsdfsdf ${mainBook.img_url} ${mainBook.book_id}")
            // url로 어떻게 이미지 세팅하는 지 내일 알아보기
            GlideApp.with(binding.homeBookImageView).load(mainBook.img_url)
                .into(binding.homeBookImageView)
        }

        fun setClick(mainBook: GetMyLibraryResponseItem) {
            binding.root.setOnClickListener {
//                libraryMainFragment.setFragmentResult("request", bundleOf("valueKey" to mainBook.book_id))
                homeFragment.goDetailFragment(mainBook.book_id)
            }
        }


    }
}
