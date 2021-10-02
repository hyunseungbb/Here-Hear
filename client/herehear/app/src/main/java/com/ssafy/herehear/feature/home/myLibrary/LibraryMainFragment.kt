package com.ssafy.herehear.feature.home.myLibrary

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentLibraryMainBinding
import com.ssafy.herehear.feature.home.myLibrary.MainRecycler.CustomMainAdapter
import com.ssafy.herehear.model.data.MainBook
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.GetMyLibraryResponse
import com.ssafy.herehear.model.network.response.GetMyLibraryResponseItem
import com.ssafy.herehear.util.GlideApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryMainFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLibraryMainBinding.inflate(inflater, container, false)
//        requestData()
//        val (data0, data1, data2) = requestData()
        val data0: MutableList<GetMyLibraryResponseItem> = mutableListOf()
        val data1: MutableList<GetMyLibraryResponseItem> = mutableListOf()
        val data2: MutableList<GetMyLibraryResponseItem> = mutableListOf()
        RetrofitClient.api.getMyLibrary().enqueue(object: Callback<GetMyLibraryResponse> {
            override fun onResponse(
                call: Call<GetMyLibraryResponse>,
                response: Response<GetMyLibraryResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("test", "응답바디 : ${response.body()}")
                    var body = response.body()
                    if (body != null) {
                        for (item in body) {
                            if (item.read_status == 0) {
                                data0.add(item)
                            } else if (item.read_status == 1) {
                                data1.add(item)
                            } else if (item.read_status == 2) {
                                data2.add(item)
                            }
                        }
                    }
                    Log.d("test", "data0 : ${data0}")
                    Log.d("test", "data1 : ${data1}")
                    Log.d("test", "data2 : ${data2}")
                    val adapter0 = CustomMainAdapter()
                    val adapter1 = CustomMainAdapter()
                    val adapter2 = CustomMainAdapter()
                    adapter0.listData = data0
                    adapter1.listData = data1
                    adapter2.listData = data2

                    binding.mainRecyclerView0.adapter = adapter0
                    binding.mainRecyclerView1.adapter = adapter1
                    binding.mainRecyclerView2.adapter = adapter2
                    binding.mainRecyclerView0.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    binding.mainRecyclerView1.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                    binding.mainRecyclerView2.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                } else {
                    Log.d("test", "응답실패 ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetMyLibraryResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
        return binding.root
    }

    fun goHomeDetailFragment() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameHome, LibraryDetailFragment())
            addToBackStack(null)
            commit()
        }
    }


    // 전체 서재 요청
//    fun requestData(): Triple<MutableList<GetMyLibraryResponseItem>, MutableList<GetMyLibraryResponseItem>, MutableList<GetMyLibraryResponseItem>> {
//        Log.d("test", "데이터 요청")
//        val data0: MutableList<GetMyLibraryResponseItem> = mutableListOf()
//        val data1: MutableList<GetMyLibraryResponseItem> = mutableListOf()
//        val data2: MutableList<GetMyLibraryResponseItem> = mutableListOf()
//
//        // status에 따라 데이터 분류
//        RetrofitClient.api.getMyLibrary().enqueue(object: Callback<GetMyLibraryResponse> {
//            override fun onResponse(
//                call: Call<GetMyLibraryResponse>,
//                response: Response<GetMyLibraryResponse>
//            ) {
//                if (response.isSuccessful) {
//                    Log.d("test", "응답바디 : ${response.body()}")
//                    var body = response.body()
//                    if (body != null) {
//                        for (item in body) {
//                            if (item.read_status == 0) {
//                                data0.add(item)
//                            } else if (item.read_status == 1) {
//                                data1.add(item)
//                            } else if (item.read_status == 2) {
//                                data2.add(item)
//                            }
//                        }
//                    }
//                    Log.d("test", "data0 : ${data0}")
//                    Log.d("test", "data1 : ${data1}")
//                    Log.d("test", "data2 : ${data2}")
//                } else {
//                    Log.d("test", "응답실패 ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<GetMyLibraryResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//        })
//
//        return Triple(data0, data1, data2)
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }
}