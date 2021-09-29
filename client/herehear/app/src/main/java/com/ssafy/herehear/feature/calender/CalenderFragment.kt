package com.ssafy.herehear.feature.calender

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentCalenderBinding
import com.ssafy.herehear.feature.calender.main.MainCalenderFragment
import com.ssafy.herehear.feature.home.readmode.ReadModeFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
val mainCalenderFragment = MainCalenderFragment()
val readModeFragment = ReadModeFragment()
/**
 * A simple [Fragment] subclass.
 * Use the [CalenderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalenderFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentCalenderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("mytest", "누름")
        val binding = FragmentCalenderBinding.inflate(inflater, container, false)

        // 처음 생성시 메인캘린더프래그먼트를 프레임에 추가
        childFragmentManager.beginTransaction()
            .add(R.id.frame, mainCalenderFragment)
            .commit()

        return binding.root
    }


    fun goReadMode() {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frame, readModeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

    fun goMain() {
        val childTransaction = childFragmentManager.beginTransaction()
        childTransaction.replace(R.id.frame, mainCalenderFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
    }

}