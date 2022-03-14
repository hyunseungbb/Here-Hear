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
import androidx.lifecycle.Observer
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
    private lateinit var adapter1: CustomMainAdapter
    private lateinit var adapter2: CustomMainAdapter
    private lateinit var adapter3: CustomMainAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        libraryMainViewModel.libraryList.observe(viewLifecycleOwner, Observer { libraryList ->
            Log.d("test", "libraryList : ${libraryList}")
            libraryList ?: return@Observer
            val data0: MutableList<Library> = mutableListOf()
            val data1: MutableList<Library> = mutableListOf()
            val data2: MutableList<Library> = mutableListOf()

            for (item in libraryList) {
                if (item.read_status == 0) {
                    data1.add(item)
                } else if (item.read_status == 1) {
                    data0.add(item)
                } else if (item.read_status == 2) {
                    data2.add(item)
                }
            }
            adapter1.updateData(data0)
            adapter2.updateData(data1)
            adapter3.updateData(data2)
        })
    }

    fun setAdapter() {
        with(binding) {
            with(mainRecyclerView0) {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = CustomMainAdapter(this@LibraryMainFragment).also {
                    adapter1 = it
                }
            }
            with(mainRecyclerView1) {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = CustomMainAdapter(this@LibraryMainFragment).also {
                    adapter2 = it
                }
            }
            with(mainRecyclerView2) {
                layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                adapter = CustomMainAdapter(this@LibraryMainFragment).also {
                    adapter3 = it
                }
            }
        }
    }

    override fun onItemClicked(library: Library) {
        CustomApplication.setBookStars(library.stars)
        CustomApplication.setBookStatus(library.read_status)
        setFragmentResult("request", bundleOf("valueKey" to library.book_id))
        homeFragment.goDetailFragment(library.book_id, library.id)
    }
}