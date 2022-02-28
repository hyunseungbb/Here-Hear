package com.ssafy.herehear.feature.home.myLibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssafy.herehear.CustomApplication

import com.ssafy.herehear.R
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.databinding.FragmentLibraryMainBinding
import com.ssafy.herehear.feature.home.HomeFragment
import com.ssafy.herehear.feature.home.myLibrary.MainRecycler.CustomMainAdapter
import com.ssafy.herehear.homeFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import java.lang.IllegalArgumentException

@AndroidEntryPoint
class LibraryMainFragment(homeFragment: HomeFragment) : Fragment(), CustomMainAdapter.OnItemClicked {
    lateinit var binding: FragmentLibraryMainBinding

    private val libraryMainViewModel: LibraryMainViewModel by viewModels()
/*  Hilt 사용 안 할 때 viewmodel 초기화
    private val libraryMainViewModel: LibraryMainViewModel by viewModels {

        val application = requireContext().applicationContext as CustomApplication

        with(application) {
            object: ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(LibraryMainViewModel::class.java)) {
                        return LibraryMainViewModel(
                            repository = libraryMainRepository,
                            uiThreadScheduler = uiThreadScheduler
                        ) as T
                    }
                    throw IllegalArgumentException("UnKnown ViewModel Class")
                }
            }
        }
    }
 */

    private val compositeDisposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        libraryMainViewModel.getMyLibrary()

        with(binding) {

            // 로컬 library table 변화 시 adapter update
            compositeDisposable += libraryMainViewModel.library.subscribeBy(onNext = { libraryList ->
                updateAdapter(libraryList)
            })
        }
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
        val adapter0 = CustomMainAdapter(this@LibraryMainFragment)
        val adapter1 = CustomMainAdapter(this@LibraryMainFragment)
        val adapter2 = CustomMainAdapter(this@LibraryMainFragment)

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

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposable.clear()
    }

    override fun onItemClicked(library: Library) {
        CustomApplication.setBookStars(library.stars)
        CustomApplication.setBookStatus(library.read_status)
        setFragmentResult("request", bundleOf("valueKey" to library.book_id))
        homeFragment.goDetailFragment(library.book_id, library.id)
    }
}