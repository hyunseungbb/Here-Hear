package com.ssafy.herehear.feature.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryMainFragment = LibraryMainFragment(this)
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameHome, libraryMainFragment)
            .commit()
    }

    fun goDetailFragment(bookId: Int, id: Int) {
        libraryDetailFragment = LibraryDetailFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameHome, libraryDetailFragment)
            .addToBackStack(null)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        val bundle = Bundle()
        bundle.putInt("valueKey", bookId)
        bundle.putInt("libraryId", id)
        libraryMainFragment.setFragmentResult("request", bundle)
    }

    fun goMainFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.frameHome, libraryMainFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

//    fun goReadModeFragment(bookId: Int, bookImgUrl: String, libraryId: Int) {
//        val childTransaction = childFragmentManager.beginTransaction()
//        childTransaction.replace(R.id.frameHome, readModeFragment)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            .commit()
//        val bundle = Bundle()
//        bundle.putInt("bookId", bookId)
//        bundle.putString("bookImgUrl", bookImgUrl)
//        bundle.putInt("libraryId", libraryId)
//        libraryMainFragment.setFragmentResult("readModeRequest", bundle)
//    }
}