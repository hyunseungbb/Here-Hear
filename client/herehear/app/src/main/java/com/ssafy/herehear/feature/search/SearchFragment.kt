package com.ssafy.herehear.feature.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentSearchBinding
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.SearchRequest
import com.ssafy.herehear.model.network.response.SearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentSearchBinding
    var searchCategoryNo: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        val items = resources.getStringArray(R.array.searchCategory)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, items)

        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position != 0) searchCategoryNo = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        val imageId = binding.bookImage


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 버튼 누를 때 호출
                val searchText = binding.searchView.query
                val searchData = SearchRequest(searchText.toString(), searchCategoryNo)


                RetrofitClient.api.search(searchText.toString(), searchCategoryNo).enqueue(object: Callback<SearchResponse>{
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.isSuccessful){
                            // need to modify
                            // To print book image
                            while (true){
                                val url = response.toString()
                                Glide.with(requireContext()).load(url).into(binding.bookImage)
                            }

                        }
                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        Log.d("onQueryTextSubmitFail", "fail")
                    }
                })
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출
                return true
            }
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}