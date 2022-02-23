package com.ssafy.herehear.feature.home.myLibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.herehear.CustomApplication

import com.ssafy.herehear.R
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.databinding.FragmentLibraryMainBinding
import com.ssafy.herehear.feature.home.myLibrary.MainRecycler.CustomMainAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class LibraryMainFragment : Fragment() {
    lateinit var binding: FragmentLibraryMainBinding

    private val libraryMainViewModel: LibraryMainViewModel by viewModels()

    // 프로바이더 만들어서 하자
//    private val libraryMainViewModel: LibraryMainViewModel by viewModels {
//
//        val application = requireContext().applicationContext as CustomApplication
//
//        with(application) {
//            object: ViewModelProvider.Factory {
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    if (modelClass.isAssignableFrom(LibraryMainViewModel::class.java)) {
//                        return LibraryMainViewModel(
//                            repository = libraryMainRepository,
//                            uiThreadScheduler = uiThreadScheduler
//                        ) as T
//                    }
//                    throw IllegalArgumentException("UnKnown ViewModel Class")
//                }
//            }
//        }
//    }

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryMainBinding.inflate(inflater, container, false)

        // 임시. 처음에 그냥 fetch하기
        libraryMainViewModel.getMyLibrary()

        with(binding) {

            // 로컬 library table 변화 시 adapter update
            compositeDisposable += libraryMainViewModel.library.subscribeBy(onNext = { libraryList ->
                Log.d("test", "libraryList : ${libraryList}")
                updateAdapter(libraryList)
            })

            // fetch하는 경우.
            // 로컬 데이터베이스에 아무것도 없을 때 fetch해온다.
            // 간단하게 - 서버 db의 내 라이브러리 데이터에 변화가 있을 때마다 fetch해온다.
            // 이 fragement 외의 다른 ui컨트롤러에서 library 데이터변화가 있을 수 있다.
            // 이경우 해당 viewmodel에서 libraryMainRepository 의존성을 가져야 한다.
            // viewmodel이 두 개의 repository를 가져도 괜찮나? 에 대한 의문
        }

        return binding.root
    }

    fun updateAdapter(librayList: List<Library>) {
        val data0: MutableList<Library> = mutableListOf()
        val data1: MutableList<Library> = mutableListOf()
        val data2: MutableList<Library> = mutableListOf()

        for (item in librayList) {
            if (item.read_status == 0) {
                data1.add(item)
            } else if (item.read_status == 1) {
                data0.add(item)
            } else if (item.read_status == 2) {
                data2.add(item)
            }
        }
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

    }

    fun goHomeDetailFragment() {
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.frameHome, LibraryDetailFragment())
            addToBackStack(null)
            commit()
        }
    }

}