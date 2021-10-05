package com.ssafy.herehear.feature.search.ui.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainSearchBinding
import com.ssafy.herehear.feature.search.SearchFragment
import com.ssafy.herehear.feature.search.adapater.BookUrl
import com.ssafy.herehear.feature.search.adapater.SearchAdapter
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.SearchRequest
import com.ssafy.herehear.model.network.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainSearchFragment : Fragment() {

    private var searchCategoryNo: Int = 1
    private lateinit var binding: FragmentMainSearchBinding
    private var urlList: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainSearchBinding.inflate(inflater, container, false)

        // spinner (드롭다운) 바인딩
        val items = resources.getStringArray(R.array.searchCategory)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = adapter

        // 드롭다운에서 선택한 정보 저장 (텍스트 입력으로 검색했을 때의 결과를 다르게 받기 위해)
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) searchCategoryNo = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val url = "http://image.aladin.co.kr/product/10789/58/cover/8964359208_1.jpg"
        Glide.with(requireContext()).load(url).override(150, 200).into(binding.imageView2)

        // 검색바에서 텍스트 입력시 수행하는 함수
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 누를 때 호출
                val searchText = binding.searchView.query
                val searchData = SearchRequest(searchText.toString(), searchCategoryNo)


                RetrofitClient.api.search(searchText.toString(), searchCategoryNo).enqueue(object:
                    Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.isSuccessful){
                            // need to modify
                            // To print book image
                            while (true){
                                urlList.add(response.toString())

                            }

//                            // 책 이미지 클릭시 goInfo 로 책정보 화면으로 가기
//                            binding.bookImage.setOnClickListener{
//                                (parentFragment as SearchFragment).goInfo()
//                            }
                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        Log.d("onQueryTextSubmitFail", "fail")
                    }
                })

                for (index in 0..urlList.size){
                    Glide.with(requireContext()).load(url).into(binding.bookImage)
                }

                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출
                return true
            }
        })

        // 책 이미지 클릭시 goInfo 로 책정보 화면으로 가기
        binding.bookImage.setOnClickListener{
            (parentFragment as SearchFragment).goInfo()
        }

        // recycler adapter를 통한 바인딩
        val data:MutableList<BookUrl> = loadData(url)
        var recyclerAdapter = SearchAdapter()
        recyclerAdapter.listData = data
        binding.recycler.adapter = recyclerAdapter
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 3)

        return binding.root
    }

    // recycler 이용하기 위한 데이터로드 함수
    fun loadData(url: String): MutableList<BookUrl>{
        val data: MutableList<BookUrl> = mutableListOf()

        for (index in 1..10){
            var bookUrl = BookUrl(url)
            data.add(bookUrl)
        }

        return data
    }

}