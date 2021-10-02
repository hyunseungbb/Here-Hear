package com.ssafy.herehear.feature.home.myLibrary.MainRecycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.herehear.databinding.HomeBookRecyclerBinding

import com.ssafy.herehear.model.data.MainBook
import com.ssafy.herehear.model.network.response.GetMyLibraryResponseItem
import com.ssafy.herehear.util.GlideApp

class CustomMainAdapter: RecyclerView.Adapter<Holder>() {

    var listData = mutableListOf<GetMyLibraryResponseItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = HomeBookRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d("test", "onBindViewHolder! ${position}")
        val mainBook = listData.get(position)
        holder.setImage(mainBook)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder(val binding: HomeBookRecyclerBinding): RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
//            Log.d("test", "클릭된 책 id : ${binding.homeBookIdTextView}")
        }
    }

    // 화면에 데이터 세팅
    fun setImage(mainBook: GetMyLibraryResponseItem) {
        Log.d("test", "dfsdfsdf ${mainBook.img_url} ${mainBook.book_id}")
        // url로 어떻게 이미지 세팅하는 지 내일 알아보기
        GlideApp.with(binding.homeBookImageView).load(mainBook.img_url)
            .override(200, 300)
            .into(binding.homeBookImageView)
        binding.homeBookIdTextView.text = mainBook.book_id.toString()
//        binding.homeBookImageView.setImageURI((mainBook.img_url))
    }
}