package com.ssafy.herehear.feature.search.ui.search

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.ssafy.herehear.databinding.FragmentSearchInfoBinding
import com.ssafy.herehear.feature.search.SearchFragment
import com.ssafy.herehear.feature.search.adapater.SearchDetailAdapter
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchInfoFragment : Fragment() {

    private lateinit var binding: FragmentSearchInfoBinding
    private var flag = false
    private var libraryId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchInfoBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("request") { key, bundle ->
            val bookId = bundle.getInt("valueKey")
            var url = "books/${bookId}"
            RetrofitClient.api.getSearchDetail(url)
                .enqueue(object : Callback<SearchDetailResponse> {
                    override fun onResponse(
                        call: Call<SearchDetailResponse>,
                        response: Response<SearchDetailResponse>
                    ) {
                        if (response.isSuccessful) {
                            Log.d("detail", "${response.body()}")
                            var body = response.body()
                            if (body != null) {
                                setView(body)
                                getComments(body.id)
                            }

                            // 내 서재에 등록된 책인지 체크 true : 등록, false : 미등록
                            getMyLibraryBooks(bookId)

                        } else {
                            Log.d("response", "response is null")
                        }

                    }

                    override fun onFailure(call: Call<SearchDetailResponse>, t: Throwable) {
                        t.printStackTrace()
                    }

                })


        }


        // 뒤로가기 버튼을 눌렀을 때
        binding.searchInfoBack.setOnClickListener {
            (parentFragment as SearchFragment).goMainFragment()
        }


    }


    // 렌더링 함수
    fun setView(body: SearchDetailResponse) {
        // 책 표지 렌더링
        Glide.with(binding.bookCover).load(body.img_url).into(binding.bookCover)

        binding.bookTitle.text = body.title
        binding.bookDescription.text = body.description

        var stars = 0
        if (body.stars_count != 0){
            stars = body.stars_sum / body.stars_count
        }
        when (stars) {
            0 -> {
            }
            1 -> binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
            2 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
            }
            3 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star3.setColorFilter(Color.parseColor("#FFCE31"))
            }
            4 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star3.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star4.setColorFilter(Color.parseColor("#FFCE31"))
            }
            5 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star3.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star4.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star5.setColorFilter(Color.parseColor("#FFCE31"))
            }
        }


    }

    // 댓글 렌더링 함수
    fun getComments(bookId: Long) {
        var url = "comment/${bookId}"
        RetrofitClient.api.getAllComments(url).enqueue(object : Callback<AllCommentsResponse> {
            override fun onResponse(
                call: Call<AllCommentsResponse>,
                response: Response<AllCommentsResponse>
            ) {
                if (response.isSuccessful) {
                    var bookData = mutableListOf<AllCommentsResponseItem>()
                    var body = response.body()

                    if (body != null) {
                        for (item in body) {
                            bookData.add(item)
                        }
                    }

                    // recycler adapter를 통한 바인딩
                    var recyclerAdapter = SearchDetailAdapter()
                    recyclerAdapter.listData = bookData
                    binding.bookCommentRecycler.adapter = recyclerAdapter
                    binding.bookCommentRecycler.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                }
            }

            override fun onFailure(call: Call<AllCommentsResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    // 내 서재에 등록된 책인지 확인하는 함수 return - true:등록 , false:미등록
    private fun getMyLibraryBooks(bookId: Int) {
        var result = false
        RetrofitClient.api.getMyLibrary().enqueue(object : Callback<GetMyLibraryResponse> {
            override fun onResponse(
                call: Call<GetMyLibraryResponse>,
                response: Response<GetMyLibraryResponse>
            ) {
                if (response.isSuccessful) {
                    var bookData = mutableListOf<GetMyLibraryResponseItem>()
                    var body = response.body()

                    if (body != null) {
                        for (item in body) {
                            bookData.add(item)
                            if (bookId == item.book_id) {
                                libraryId = item.id
                                Log.d("함수 내 libraryId", "${libraryId}")
                                result = true
                            }
                        }
                        flag = result

                        if (flag){
                            binding.good.setColorFilter(Color.parseColor("#C73E3E"))
                        } else {
                            binding.good.setColorFilter(Color.parseColor("#EEDCDC"))
                        }

                        binding.good.setOnClickListener {
                            registerBookClick(bookId, libraryId, flag)
                        }
                    }

                }
            }

            override fun onFailure(call: Call<GetMyLibraryResponse>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    // 내 서재에 책을 등록하는 함수
    fun registerBookClick(bookId: Int, libraryId: Int, isRegistry: Boolean) {
        // 좋아요를 눌렀을 때
        Log.d("click", "클릭했냐 ${isRegistry}")
        if (!isRegistry) {
            var url = "libraries/${bookId}"
            RetrofitClient.api.registerBook(url).enqueue(object : Callback<RegisterBookResponse> {
                override fun onResponse(
                    call: Call<RegisterBookResponse>,
                    response: Response<RegisterBookResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("책등록", "성공")
                        Toast.makeText(activity, "서재에 책이 등록되었습니다.", Toast.LENGTH_SHORT).show()
                        flag = true
                        if (flag){
                            binding.good.setColorFilter(Color.parseColor("#C73E3E"))
                        }
                    }
                }

                override fun onFailure(
                    call: Call<RegisterBookResponse>,
                    t: Throwable
                ) {
                    t.printStackTrace()
                }

            })
        } else {
            var url = "libraries/${libraryId}"
            RetrofitClient.api.deleteBook(url).enqueue(object : Callback<DeleteBookResponse> {
                override fun onResponse(
                    call: Call<DeleteBookResponse>,
                    response: Response<DeleteBookResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("책삭제", "성공")
                        Toast.makeText(activity, "서재에서 책을 제거하였습니다.", Toast.LENGTH_SHORT).show()
                        flag = false
                        if (!flag){
                            binding.good.setColorFilter(Color.parseColor("#EEDCDC"))
                        }
                    }
                }

                override fun onFailure(call: Call<DeleteBookResponse>, t: Throwable) {
                    t.printStackTrace()
                }


            })
        }

    }
}