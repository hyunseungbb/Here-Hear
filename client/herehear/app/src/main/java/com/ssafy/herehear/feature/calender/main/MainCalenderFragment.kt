package com.ssafy.herehear.feature.calender.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainCalenderBinding
import com.ssafy.herehear.feature.calender.CalenderFragment

class MainCalenderFragment : Fragment() {

    lateinit var binding: FragmentMainCalenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCalenderBinding.inflate(inflater, container, false)

        // goReadMode 버튼 클릭시 리드모드로 가기
        binding.btnGoReadMode.setOnClickListener {
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnGoReadMode.setOnClickListener {
            (parentFragment as CalenderFragment).goReadMode()
        }

    }
}