package com.ssafy.herehear.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentHomeBinding
import com.ssafy.herehear.feature.calender.readModeFragment
import com.ssafy.herehear.feature.home.myLibrary.LibraryDetailFragment
import com.ssafy.herehear.feature.home.myLibrary.LibraryMainFragment
import dagger.hilt.android.AndroidEntryPoint

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    private lateinit var libraryMainFragment: LibraryMainFragment
    private lateinit var libraryDetailFragment: LibraryDetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        libraryMainFragment = LibraryMainFragment(this)
        childFragmentManager.beginTransaction()
            .add(R.id.frameHome, libraryMainFragment)
            .addToBackStack(null)
            .commit()

        return binding.root
    }

    fun goDetailFragment(bookId: Int, id: Int) {
        libraryDetailFragment = LibraryDetailFragment()
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frameHome, libraryDetailFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        val bundle = Bundle()
        bundle.putInt("valueKey", bookId)
        bundle.putInt("libraryId", id)
        libraryMainFragment.setFragmentResult("request", bundle)
    }
    fun goMainFragment() {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frameHome, libraryMainFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun goReadModeFragment(bookId: Int, bookImgUrl: String, libraryId: Int) {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frameHome, readModeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        val bundle = Bundle()
        bundle.putInt("bookId", bookId)
        bundle.putString("bookImgUrl", bookImgUrl)
        bundle.putInt("libraryId", libraryId)
        libraryMainFragment.setFragmentResult("readModeRequest", bundle)
    }
}