package com.ssafy.herehear.feature.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentSearchBinding
import com.ssafy.herehear.feature.search.ui.search.MainSearchFragment
import com.ssafy.herehear.feature.search.ui.search.SearchInfoFragment
import com.ssafy.herehear.feature.search.ui.search.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

lateinit var mainSearchFragment: MainSearchFragment
lateinit var searchInfoFragment: SearchInfoFragment


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainSearchFragment = MainSearchFragment()
        childFragmentManager.beginTransaction()
            .add(R.id.frameSearch, mainSearchFragment)
            .commit()
    }

    fun goInfoFragment(bookId: Int, bookImageUrl: String, bookTitle: String) {
        val childTransaction = childFragmentManager.beginTransaction()
        searchInfoFragment = SearchInfoFragment()
        childTransaction
            .replace(R.id.frameSearch, searchInfoFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
//        val bundle = Bundle()
//        bundle.putInt("bookId", bookId.toInt())
//        bundle.putString("bookImageUrl", bookImageUrl)
//        bundle.putString("bookTitle", bookTitle)
//        mainSearchFragment.setFragmentResult("readModeRequest", bundle)
    }

    fun goMainFragment() {
        val childTransaction = childFragmentManager.beginTransaction()
        mainSearchFragment = MainSearchFragment()
        childTransaction.replace(R.id.frameSearch, mainSearchFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}