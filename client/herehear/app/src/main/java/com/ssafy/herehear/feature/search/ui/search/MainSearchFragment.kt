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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainSearchBinding
import com.ssafy.herehear.feature.search.adapater.SearchAdapter
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.RetrofitClientRecommend
import com.ssafy.herehear.data.network.response.SearchResponse
import com.ssafy.herehear.data.network.response.SearchResponseItem
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MainSearchFragment : Fragment() {

    private var type: String = "title"
    private lateinit var binding: FragmentMainSearchBinding
    private val searchViewModel: SearchViewModel by viewModels({requireParentFragment()})
//    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var searchAdapter: SearchAdapter
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

        with(binding) {

            with(recycler) {
                layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
                adapter = SearchAdapter().also {
                    searchAdapter = it
                }
            }

            with(searchView) {
                setQuery("", false)
                setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        if (query != null) {
                            searchViewModel.getSearchResult(query)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return true
                    }
                })
            }

            with(spinner) {
                val items = resources.getStringArray(R.array.searchCategory)
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
                this.adapter = adapter
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        when (position){
                            0 -> type = "title"
                            1 -> type = "author"
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }
                }
            }

            searchViewModel.bookList.observe(viewLifecycleOwner, Observer { bookList ->
                bookList ?: return@Observer
                searchAdapter.updateData(bookList)
            })

        }

        // spinner (????????????) ?????????
//        val items = resources.getStringArray(R.array.searchCategory)
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)
//        binding.spinner.adapter = adapter
//
//        // ?????????????????? ????????? ?????? ?????? (????????? ???????????? ???????????? ?????? ????????? ????????? ?????? ??????)
//        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                when (position){
//                    0 -> type = "title"
//                    1 -> type = "author"
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }

//        binding.searchView.setQuery ( "", false)
//        // ??????????????? ????????? ????????? ???????????? ??????
//        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                // ?????? ?????? ?????? ??? ??????
//                val searchText = binding.searchView.query
//
////                RetrofitClient.api.getSearch(type, searchText.toString()).enqueue(object:
////                    Callback<SearchResponse> {
////                    override fun onResponse(
////                        call: Call<SearchResponse>,
////                        response: Response<SearchResponse>
////                    ) {
////                        if (response.isSuccessful){
////                            var bookData: MutableList<SearchResponseItem> = mutableListOf()
////                            var body = response.body()
////
////                            setView(bookData, body)
////                        }
////
////                    }
////
////                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
////                        t.printStackTrace()
////                    }
////                })
//
//
//                return true
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//
//                // ??????????????? ????????? ????????? ????????? ????????? ??????
//                return true
//            }
//        })

    }

    fun func(bookId: Int) {
        searchViewModel.bookId = bookId
    }
//    private fun setView(bookData: MutableList<SearchResponseItem>, body: SearchResponse?){
//        if (body != null){
//            for (item in body){
//                bookData.add(item)
//            }
//        } else {
//            Log.d("error", "response is null")
//        }
//
//        // recycler adapter??? ?????? ?????????
//        var recyclerAdapter = SearchAdapter()
//        recyclerAdapter.listData = bookData
//        binding.recycler.adapter = recyclerAdapter
//        binding.recycler.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
//    }

}