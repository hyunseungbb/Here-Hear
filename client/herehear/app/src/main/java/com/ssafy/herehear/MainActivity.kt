package com.ssafy.herehear

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.ActivityMainBinding

import com.ssafy.herehear.feature.calender.CalenderFragment
import com.ssafy.herehear.feature.home.HomeFragment
import com.ssafy.herehear.feature.home.readmode.ReadModeFragment
import com.ssafy.herehear.feature.home.readmode.audiobook.Camera2Activity
import com.ssafy.herehear.feature.home.readmode.audiobook.CameraActivity
import com.ssafy.herehear.feature.home.readmode.paperbook.TimerActivity
import com.ssafy.herehear.feature.mypage.MyPageFragment
import com.ssafy.herehear.feature.search.SearchFragment

lateinit var homeFragment : HomeFragment

class MainActivity : AppCompatActivity() {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var getResultText: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//        val tabFragmentList = listOf(HomeFragment(), CalenderFragment(), SearchFragment(), MyPageFragment())
//        val adapter = FragmentAdapter(this)
//        adapter.fragmentList = tabFragmentList
//        binding.viewPager.adapter = adapter

        // 탭과 프래그먼트들을 연결하는 메서드
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//        }.attach()
        val transaction = supportFragmentManager.beginTransaction()
        homeFragment = HomeFragment()
        transaction.add(R.id.frameMain, homeFragment)
        transaction.addToBackStack(null)
        transaction.commit()
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.home)
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.calender)
        binding.tabLayout.getTabAt(2)?.setIcon(R.drawable.search)
        binding.tabLayout.getTabAt(3)?.setIcon(R.drawable.mypage)

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                switchFragment(tab!!.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })


        getResultText = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val message = result.data?.getIntExtra("bookId", 0)
            }
        }

    }

    fun switchFragment(flag: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when(flag) {
            0 -> {
                homeFragment = HomeFragment()
                transaction.replace(R.id.frameMain, homeFragment)
            }
            1 -> {
                transaction.replace(R.id.frameMain, CalenderFragment())
            }
            2 -> {
                transaction.replace(R.id.frameMain, SearchFragment())
            }
            3 -> {
                transaction.replace(R.id.frameMain, MyPageFragment())
            }
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun goCameraActivity(bookId: Int) {
        val intent = Intent(this, Camera2Activity::class.java)
        intent.putExtra("bookId", bookId)
        getResultText.launch(intent)
    }

    fun goTimerActivity(bookId: Int) {
        val intent = Intent(this, TimerActivity::class.java)
        intent.putExtra("bookId", bookId)
        getResultText.launch(intent)
    }

    fun goIntroAcitivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }


//    fun setFragment() {
//
//    }
//
//    fun goReadMode() {
//        Log.d("mytest", "여기로 오나??")
//        val readModeFragment = ReadModeFragment()
//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.add(R.id.viewPager, readModeFragment)
//        Log.d("test", "추가가 되나?")
//        transaction.addToBackStack("readmode")
//        transaction.commit()
//    }
//
//    fun goBack() {
//        onBackPressed()
//    }

}