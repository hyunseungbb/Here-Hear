package com.ssafy.herehear.feature.home.myLibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentLibraryDetailBinding
import com.ssafy.herehear.feature.home.myLibrary.MainRecycler.CustomDetailAdapter
import com.ssafy.herehear.homeFragment
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.*
import com.ssafy.herehear.util.GlideApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LibraryDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var binding: FragmentLibraryDetailBinding
class LibraryDetailFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("request") {key, bundle ->

            val bookId = bundle.getInt("valueKey")
            var url = "books/${bookId}"
            RetrofitClient.api.getHomeBookDetail(url).enqueue(object: Callback<BookDetailResponse>{
                override fun onResponse(
                    call: Call<BookDetailResponse>,
                    response: Response<BookDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { setView(it) }
                    } else {
                        Toast.makeText(activity, "kslfjlk", Toast.LENGTH_SHORT).show()
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

            binding.readDoneButton.setOnClickListener {
                // 요청 보내기
                val data = UpdateBookStatusRequest(bookId, 2, 0)
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
                homeFragment.goReadModeFragment(bookId)
            }
        }

        binding.homeDetailBackButton.setOnClickListener {
            homeFragment.goMainFragment()
        }
    }

    fun setView(body: BookDetailResponse) {
        GlideApp.with(binding.detailBookImageView).load(body.img_url)
            .into(binding.detailBookImageView)
        binding.descriptionTextView.text = body.description
        binding.ratingBar.numStars = body.stars_count
        binding.detailBookTitleTextView.text = body.title

    }

}