package com.ssafy.herehear.feature.home.readmode

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.setFragmentResultListener
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentHomeBinding
import com.ssafy.herehear.databinding.FragmentReadModeBinding
import com.ssafy.herehear.feature.calender.CalenderFragment
import com.ssafy.herehear.feature.home.HomeFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReadModeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReadModeFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var binding: FragmentReadModeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentReadModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentResultListener("readModeRequest") {key, bundle ->
            val bookId = bundle.getInt("bookId")
            val bookImgUrl = bundle.getString("bookImgUrl")

            binding.readModeBackButton.setOnClickListener{
                (parentFragment as HomeFragment).goDetailFragment(bookId)
            }

            binding.audioButton.setOnClickListener {
                mainActivity.goCameraActivity(bookId)
            }

            binding.paperButton.setOnClickListener {
                if (bookImgUrl != null) {
                    mainActivity.goTimerActivity(bookId, bookImgUrl)
                } else {
                    mainActivity.goTimerActivity(bookId, "")
                }
            }
        }
    }
}