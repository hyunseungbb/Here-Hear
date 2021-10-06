package com.ssafy.herehear.feature.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentSearchBinding
import com.ssafy.herehear.feature.search.ui.search.MainSearchFragment
import com.ssafy.herehear.feature.search.ui.search.SearchInfoFragment

val mainSearchFragment = MainSearchFragment()
val searchInfoFragment = SearchInfoFragment()

class SearchFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val binding = FragmentSearchBinding.inflate(inflater, container, false)

        childFragmentManager.beginTransaction()
            .add(R.id.frameSearch, mainSearchFragment)
            .commit()

        return binding.root
    }

    fun goInfoFragment(bookId: Long, bookImageUrl: String, bookTitle: String) {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frameSearch, searchInfoFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        val bundle = Bundle()
        bundle.putLong("bookId", bookId)
        bundle.putString("bookImageUrl", bookImageUrl)
        bundle.putString("bookTitle", bookTitle)
        mainSearchFragment.setFragmentResult("readModeRequest", bundle)
    }

    fun goMainFragment() {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frameSearch, mainSearchFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}