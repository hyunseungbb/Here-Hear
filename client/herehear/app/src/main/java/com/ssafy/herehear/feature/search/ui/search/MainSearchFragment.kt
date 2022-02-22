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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainSearchBinding
import com.ssafy.herehear.feature.search.adapater.SearchAdapter
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.RetrofitClientRecommend
import com.ssafy.herehear.data.network.response.SearchResponse
import com.ssafy.herehear.data.network.response.SearchResponseItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainSearchFragment : Fragment() {

    private var type: String = "title"
    private lateinit var binding: FragmentMainSearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainSearchBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 검색하기 전에 보여질 화면 - 사용자에 맞는 추천책 렌더링
        val username = HereHear.prefs.getString("userId", "")
        Log.d("username", "${username}")
        RetrofitClientRecommend.api.getRecommend(username).enqueue(object : Callback<SearchResponse>{
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful){
                    Log.d("시작화면", "${response.body()}")
                    var bookData: MutableList<SearchResponseItem> = mutableListOf()
                    var body = response.body()

                    setView(bookData, body)
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                t.printStackTrace()
            }


        })

        // spinner (드롭다운) 바인딩
        val items = resources.getStringArray(R.array.searchCategory)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
        binding.spinner.adapter = adapter

        // 드롭다운에서 선택한 정보 저장 (텍스트 입력으로 검색했을 때의 결과를 다르게 받기 위해)
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position){
                    0 -> type = "title"
                    1 -> type = "author"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.searchView.setQuery ( "", false)
        // 검색바에서 텍스트 입력시 수행하는 함수
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 누를 때 호출
                val searchText = binding.searchView.query

                RetrofitClient.api.getSearch(type, searchText.toString()).enqueue(object:
                    Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.isSuccessful){
                            var bookData: MutableList<SearchResponseItem> = mutableListOf()
                            var body = response.body()

                            setView(bookData, body)
                        }

                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        t.printStackTrace()
                    }
                })


                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출
                return true
            }
        })


    }

    private fun setView(bookData: MutableList<SearchResponseItem>, body: SearchResponse?){
        if (body != null){
            for (item in body){
                bookData.add(item)
            }
        } else {
            Log.d("error", "response is null")
        }

        // recycler adapter를 통한 바인딩
        var recyclerAdapter = SearchAdapter()
        recyclerAdapter.listData = bookData
        binding.recycler.adapter = recyclerAdapter
        binding.recycler.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
    }

}