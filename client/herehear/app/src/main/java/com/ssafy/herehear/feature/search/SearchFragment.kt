package com.ssafy.herehear.feature.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
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
            .add(R.id.search, mainSearchFragment)
            .commit()

        return binding.root
    }

    fun goInfo() {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.search, searchInfoFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun goMain() {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.search, mainSearchFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}