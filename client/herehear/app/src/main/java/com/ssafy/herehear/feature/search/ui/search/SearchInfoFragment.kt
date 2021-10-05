package com.ssafy.herehear.feature.search.ui.search

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentMainSearchBinding
import com.ssafy.herehear.databinding.FragmentSearchInfoBinding
import com.ssafy.herehear.feature.search.SearchFragment

class SearchInfoFragment : Fragment() {

    // stars : 별점 (지금은 임시로 3으로 해놓음)
    private val stars: Int = 3
    private lateinit var binding: FragmentSearchInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchInfoBinding.inflate(inflater, container, false)

        when (stars){
            0 -> {}
            1 -> binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
            2 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
            }
            3 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star3.setColorFilter(Color.parseColor("#FFCE31"))
            }
            4 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star3.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star4.setColorFilter(Color.parseColor("#FFCE31"))
            }
            5 -> {
                binding.star1.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star2.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star3.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star4.setColorFilter(Color.parseColor("#FFCE31"))
                binding.star5.setColorFilter(Color.parseColor("#FFCE31"))
            }
        }

        // 뒤로가기 버튼을 눌렀을 때
        binding.searchInfoBack.setOnClickListener{
            (parentFragment as SearchFragment).goMain()
        }

        // 좋아요를 눌렀을 때
        binding.good.setOnClickListener{

        }

        //

        return binding.root
    }
}