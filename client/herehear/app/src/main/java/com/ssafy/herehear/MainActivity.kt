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
    private var backButtonTime = 0L
    private var fragmentCount = 0

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
//        transaction.addToBackStack(null)
        transaction.commit()
    }


    fun goCameraActivity(bookId: Int, libraryId: Int) {
        val intent = Intent(this, Camera2Activity::class.java)
        intent.putExtra("bookId", bookId)
        intent.putExtra("libraryId", libraryId)
//        startActivity(intent)
//        finish()
        getResultText.launch(intent)
    }

    fun goTimerActivity(bookId: Int, bookImgUrl: String, libraryId: Int) {
        val mainIntent = Intent(this, TimerActivity::class.java)
        mainIntent.putExtra("bookId", bookId)
        mainIntent.putExtra("bookImgUrl", bookImgUrl)
        mainIntent.putExtra("libraryId", libraryId)
        startActivity(mainIntent)
        finish()
    }

    fun goIntroAcitivity() {
        val intent = Intent(this, IntroActivity::class.java)
        startActivity(intent)
        finish()
    }

//    interface onKeyBackPressedListener {
//        fun onBackKey()
//    }
//    private lateinit var mOnKeyBackPressedListener: onKeyBackPressedListener
//    fun setOnKeyBackPressedListener(listener: onKeyBackPressedListener) {
//        mOnKeyBackPressedListener = listener
//    }
//    override fun onBackPressed() {
//        super.onBackPressed()
//        val currentTime = System.currentTimeMillis()
//        val gapTime = currentTime - backButtonTime
//
//        if (gapTime in 0..2000) {
//            if (this.fragmentCount <= 0) finish()
//        } else {
//            backButtonTime = currentTime
//            if(this.fragmentCount <= 1) {
//                Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                super.onBackPressed()
//            }
//        }
//    }
//
//    fun increaseFragmentCount() {
//        this.fragmentCount += 1
//    }
//    fun decreaseFragmentCount() {
//        this.fragmentCount -= 1
//    }
}