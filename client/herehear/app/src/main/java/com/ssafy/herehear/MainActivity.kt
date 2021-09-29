package com.ssafy.herehear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityMainBinding
import com.ssafy.herehear.feature.calender.CalenderFragment
import com.ssafy.herehear.feature.home.HomeFragment
import com.ssafy.herehear.feature.mypage.MyPageFragment
import com.ssafy.herehear.feature.search.SearchFragment


class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val tabFragmentList = listOf(HomeFragment(), CalenderFragment(), SearchFragment(), MyPageFragment())
        val adapter = FragmentAdapter(this)
        adapter.fragmentList = tabFragmentList
        binding.viewPager.adapter = adapter

        // 탭과 프래그먼트들을 연결하는 메서드
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
        }.attach()

        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.arrow)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.calender)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.search)
        binding.tabLayout.getTabAt(3)?.setIcon(R.drawable.mypage)
    }
}