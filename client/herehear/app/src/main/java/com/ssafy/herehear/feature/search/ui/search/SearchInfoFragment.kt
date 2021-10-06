package com.ssafy.herehear.feature.search.ui.search

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.bumptech.glide.Glide
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainSearchBinding
import com.ssafy.herehear.databinding.FragmentSearchInfoBinding
import com.ssafy.herehear.feature.search.SearchFragment
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.SearchDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchInfoFragment : Fragment() {

    private lateinit var binding: FragmentSearchInfoBinding

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

        var bookData: SearchDetailResponse? = null

        setFragmentResultListener("request"){key, bundle ->
            val bookId = bundle.getLong("valueKey")
            var url = "books/${bookId}"
            RetrofitClient.api.getSearchDetail(url).enqueue(object: Callback<SearchDetailResponse>{
                override fun onResponse(
                    call: Call<SearchDetailResponse>,
                    response: Response<SearchDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        if (bookData != null) {
                            bookData.description = response.body()?.description.toString()
                            bookData.id = response.body()?.id!!
                            bookData.img_url = response.body()?.img_url.toString()
                            bookData.stars_count = response.body()?.stars_count!!
                            bookData.stars_sum = response.body()?.stars_sum!!
                            bookData.title = response.body()?.title.toString()
                        }
                    }
                }

                override fun onFailure(call: Call<SearchDetailResponse>, t: Throwable) {
                    t.printStackTrace()
                }

            })



        }


        if (bookData != null) {
            when (bookData.stars_count){
                0 -> {}
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

        // 뒤로가기 버튼을 눌렀을 때
        binding.searchInfoBack.setOnClickListener{
            (parentFragment as SearchFragment).goMainFragment()
        }

        // 좋아요를 눌렀을 때
        binding.good.setOnClickListener{

        }



    }

    fun setView(body: SearchDetailResponse) {
        Glide.with(binding.bookCover).load(body.img_url).into(binding.bookCover)

        binding.bookTitle.text = body.title
        binding.bookDescription.text = body.description


    }
}