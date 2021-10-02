package com.ssafy.herehear.feature.home.myLibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentLibraryDetailBinding
import com.ssafy.herehear.homeFragment
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.AllCommentsResponse
import com.ssafy.herehear.model.network.response.BookDetailResponse
import com.ssafy.herehear.util.GlideApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LibraryDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var binding: FragmentLibraryDetailBinding
class LibraryDetailFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryDetailBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("request") {key, bundle ->

            val bookId = bundle.getInt("valueKey")
            var url = "books/${bookId}"
            RetrofitClient.api.getHomeBookDetail(url).enqueue(object: Callback<BookDetailResponse>{
                override fun onResponse(
                    call: Call<BookDetailResponse>,
                    response: Response<BookDetailResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { setView(it) }
                    } else {
                        Toast.makeText(activity, "kslfjlk", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })

            url = "commet/${bookId}"
            RetrofitClient.api.getAllComments(url).enqueue(object: Callback<AllCommentsResponse> {
                override fun onResponse(
                    call: Call<AllCommentsResponse>,
                    response: Response<AllCommentsResponse>
                ) {
                    if (response.isSuccessful) {
                        var myList: AllCommentsResponse? = response.body()
                        Log.d("test", "${myList}")
                    } else {
                        Toast.makeText(activity, "kslfjlk", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AllCommentsResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

        binding.homeDetailBackButton.setOnClickListener {
            homeFragment.goMainFragment()
        }
    }

    fun setView(body: BookDetailResponse) {
        GlideApp.with(binding.detailBookImageView).load(body.img_url)
            .into(binding.detailBookImageView)
        binding.descriptionTextView.text = body.description
        binding.ratingBar.numStars = body.stars_count
        binding.detailBookTitleTextView.text = body.title

    }

}