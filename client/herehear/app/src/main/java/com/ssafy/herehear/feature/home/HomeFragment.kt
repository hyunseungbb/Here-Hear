package com.ssafy.herehear.feature.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.IntroActivity
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityLoginBinding
import com.ssafy.herehear.databinding.FragmentHomeBinding
import com.ssafy.herehear.databinding.FragmentLoginBinding
import com.ssafy.herehear.databinding.FragmentReadModeBinding
import com.ssafy.herehear.feature.calender.mainCalenderFragment
import com.ssafy.herehear.feature.calender.readModeFragment
import com.ssafy.herehear.feature.home.myLibrary.LibraryDetailFragment
import com.ssafy.herehear.feature.home.myLibrary.LibraryMainFragment
import com.ssafy.herehear.feature.home.readmode.ReadModeFragment
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.LoginRequest
import com.ssafy.herehear.model.network.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

val libraryMainFragment = LibraryMainFragment()
val libraryDetailFragment = LibraryDetailFragment()
lateinit var binding: FragmentHomeBinding

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // readmode 프래그먼트를 프레임에 일단 추가. 원래는 홈메인임
        childFragmentManager.beginTransaction()
            .add(R.id.frameHome, libraryMainFragment)
            .commit()

        return binding.root
    }

//    fun goMain() {
//        Log.d("test", "나중에는 상세페이지로 가도록 구현")
//        val childTransaction = childFragmentManager.beginTransaction()
//        childTransaction.replace(R.id.frameHome, libraryDetailFragment)
//            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
//            .addToBackStack(null)
//            .commit()
//    }

    fun goDetailFragment(bookId: Int, id: Int) {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frameHome, libraryDetailFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .addToBackStack(null)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}