package com.ssafy.herehear.feature.home.myLibrary.MainRecycler

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.databinding.HomeBookRecyclerBinding
import com.ssafy.herehear.homeFragment

class CustomMainAdapter(private val listener: OnItemClicked): RecyclerView.Adapter<CustomMainAdapter.Holder>() {

    interface OnItemClicked {
        fun onItemClicked(library: Library)
    }

    var listData = mutableListOf<Library>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = HomeBookRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding, parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d("test", "onBindViewHolder! ${position}")
        val mainBook = listData.get(position)
        holder.setImage(mainBook)
//        holder.setClick(mainBook)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(libraries: MutableList<Library>) {
        this.listData = libraries
        notifyDataSetChanged()
    }

    inner class Holder(val binding: HomeBookRecyclerBinding, val parent: ViewGroup): RecyclerView.ViewHolder(binding.root) {

        private val onClickListener: View.OnClickListener = View.OnClickListener {
            val position = adapterPosition.takeIf { it >= 0 } ?: return@OnClickListener
            val library = listData[position]
            listener.onItemClicked(library)
        }

        init {
            binding.homeBookContainer.setOnClickListener(onClickListener)
        }

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

//        fun setClick(mainBook: Library) {
//            binding.root.setOnClickListener {
//                CustomApplication.setBookStars(mainBook.stars)
//                CustomApplication.setBookStatus(mainBook.read_status)
////                libraryMainFragment.setFragmentResult("request", bundleOf("valueKey" to mainBook.book_id))
//                homeFragment.goDetailFragment(mainBook.book_id, mainBook.id)
//            }
//        }
    }

}
