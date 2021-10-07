package com.ssafy.herehear.feature.home.myLibrary

import android.app.AlertDialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.GenericTransitionOptions.with
import com.bumptech.glide.Glide
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentLibraryDetailBinding
import com.ssafy.herehear.feature.home.libraryDetailFragment
import com.ssafy.herehear.feature.home.myLibrary.MainRecycler.CustomDetailAdapter
import com.ssafy.herehear.feature.home.readmode.CommentActivity
import com.ssafy.herehear.homeFragment
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.*
import com.ssafy.herehear.util.MyGlideApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var binding: FragmentLibraryDetailBinding
class LibraryDetailFragment : Fragment() {
    lateinit var inflaterr: LayoutInflater
    lateinit var bookImgUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflaterr = inflater
        binding = FragmentLibraryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("request") {key, bundle ->

            val bookId = bundle.getInt("valueKey")
            val libraryId = bundle.getInt("libraryId")
            var url = "books/${bookId}"
            RetrofitClient.api.getHomeBookDetail(url).enqueue(object: Callback<BookDetailResponse>{
                override fun onResponse(
                    call: Call<BookDetailResponse>,
                    response: Response<BookDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        bookImgUrl = response.body()!!.img_url
                        response.body()?.let { setView(it) }
                    }
                }

                override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })

            url = "comment/${bookId}"
            RetrofitClient.api.getAllComments(url).enqueue(object: Callback<AllCommentsResponse> {
                override fun onResponse(
                    call: Call<AllCommentsResponse>,
                    response: Response<AllCommentsResponse>
                ) {
                    if (response.isSuccessful) {
                        val data: MutableList<AllCommentsResponseItem> = mutableListOf()
                        var body = response.body()
                        if (body != null) {
                            for (item in body) {
                                data.add(item)
                            }
                        }
                        val adapter = CustomDetailAdapter()
                        adapter.listData = data
                        binding.commentRecyclerView.adapter = adapter
                        binding.commentRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)

                    } else {
                        Toast.makeText(activity, "${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AllCommentsResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
            binding.ratingBar.rating = HereHear.getBookStars().toFloat()
            binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
                // 평점을 바꾸시겠습니까?
                showPopup(fl.toInt(), libraryId)
            }

            binding.readDoneButton.setOnClickListener {
                // 요청 보내기
                val data = UpdateBookStatusRequest(libraryId, 2, binding.ratingBar.rating.toInt())
                RetrofitClient.api.updateBookStatus(data).enqueue(object: Callback<UpdateBookStatusResponse> {
                    override fun onResponse(
                        call: Call<UpdateBookStatusResponse>,
                        response: Response<UpdateBookStatusResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(activity, "다 읽은 책으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("test", "#${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<UpdateBookStatusResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
            binding.goReadModeButton.setOnClickListener {
                // readmodefragment로 replace 똑같은 Home fragment를 부모프래그먼트로 가지고 있음
                // 책 id도 보내줘야함

                val data = UpdateBookStatusRequest(libraryId, 1, binding.ratingBar.rating.toInt())
                RetrofitClient.api.updateBookStatus(data).enqueue(object: Callback<UpdateBookStatusResponse> {
                    override fun onResponse(
                        call: Call<UpdateBookStatusResponse>,
                        response: Response<UpdateBookStatusResponse>
                    ) {
                        homeFragment.goReadModeFragment(bookId, bookImgUrl, libraryId)
                    }

                    override fun onFailure(call: Call<UpdateBookStatusResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
        }

        binding.homeDetailBackButton.setOnClickListener {
            homeFragment.goMainFragment()
        }
    }

    fun setView(body: BookDetailResponse) {
        Glide.with(binding.detailBookImageView).load(body.img_url)
            .into(binding.detailBookImageView)
        binding.descriptionTextView.text = body.description
        binding.detailBookTitleTextView.text = body.title

    }

    private fun showPopup(rating: Int, libraryId: Int) {
        val view = inflaterr.inflate(R.layout.alert_popup, null)

        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("평점을 등록하시겠습니까?")
            .setPositiveButton("확인") {dialog, which ->
                val bookStatus = HereHear.getBookStatus()
                val requestBody = UpdateBookStatusRequest(libraryId, bookStatus, rating)
                RetrofitClient.api.updateBookStatus(requestBody).enqueue(object: Callback<UpdateBookStatusResponse> {
                    override fun onResponse(
                        call: Call<UpdateBookStatusResponse>,
                        response: Response<UpdateBookStatusResponse>
                    ) {
                        if (response.isSuccessful) {
                            HereHear.setBookStars(binding.ratingBar.rating.toInt())
                            Toast.makeText(activity, "평점이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(activity, "등록 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<UpdateBookStatusResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })
            }
            .setNeutralButton("취소") {dialog, which ->
//                binding.ratingBar.rating = HereHear.getBookStars().toFloat()
            }
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }
}