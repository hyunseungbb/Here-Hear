package com.ssafy.herehear.feature.home.myLibrary

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentLibraryDetailBinding

import com.ssafy.herehear.feature.home.myLibrary.MainRecycler.CustomDetailAdapter
import com.ssafy.herehear.homeFragment
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.response.*
import com.ssafy.herehear.feature.home.readmode.audiobook.AudioPlayActivity
import com.ssafy.herehear.feature.home.readmode.audiobook.PlayerActivity
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var binding: FragmentLibraryDetailBinding

@AndroidEntryPoint
class LibraryDetailFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var bookImgUrl: String
    var bookId by Delegates.notNull<Int>()
    var libraryId by Delegates.notNull<Int>()
    var preRating: Float = 0F
    private val libraryDetailViewModel: LibraryDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setFragmentResultListener("request") {key, bundle ->
            bookId = bundle.getInt("valueKey")
            libraryId = bundle.getInt("libraryId")
            libraryDetailViewModel.getComments(bookId)
            libraryDetailViewModel.getDetail(bookId)
            libraryDetailViewModel.getLibrary(bookId)
//            var url = "books/${bookId}"
//
//            RetrofitClient.api.getHomeBookDetail(url).enqueue(object: Callback<BookDetailResponse>{
//                override fun onResponse(
//                    call: Call<BookDetailResponse>,
//                    response: Response<BookDetailResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        bookImgUrl = response.body()!!.img_url
//                        response.body()?.let { setView(it) }
//                    }
//                }
//
//                override fun onFailure(call: Call<BookDetailResponse>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })
//            RetrofitClient.api.getAllComments(url).enqueue(object: Callback<AllCommentsResponse> {
//                override fun onResponse(
//                    call: Call<AllCommentsResponse>,
//                    response: Response<AllCommentsResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val data: MutableList<AllCommentsResponseItem> = mutableListOf()
//                        var body = response.body()
//                        if (body != null) {
//                            for (item in body) {
//                                data.add(item)
//                            }
//                        }
//                        val adapter = CustomDetailAdapter()
//                        adapter.listData = data
//                        binding.commentRecyclerView.adapter = adapter
//                        binding.commentRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
//
//                    } else {
//                        Toast.makeText(activity, "${response.code()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<AllCommentsResponse>, t: Throwable) {
//                    t.printStackTrace()
//                }
//            })

            with(binding) {
                libraryDetailViewModel.book.observe(viewLifecycleOwner, Observer { book ->
                    book ?: return@Observer
                    Glide.with(detailBookImageView).load(book.img_url)
                        .into(detailBookImageView)
                    descriptionTextView.text = book.description
                    detailBookTitleTextView.text = book.title
                } )

                libraryDetailViewModel.commentList.observe(viewLifecycleOwner, Observer { comments ->
                    val adapter = CustomDetailAdapter()
                    adapter.listData = comments
                    commentRecyclerView.adapter = adapter
                    commentRecyclerView.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL)
                })

                libraryDetailViewModel.library.observe(viewLifecycleOwner, Observer { library ->
                    Log.d("test", "library 변화 감지 -> 스타를 바꾼다.")
                    ratingBar.rating = library.stars.toFloat()
                    preRating = library.stars.toFloat()
                })

                libraryDetailViewModel.readDoneState.observe(viewLifecycleOwner, Observer { state ->
                    if (state == true) {
                        Toast.makeText(activity, "다 읽은 책으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "다 읽은 책으로 등록에 실패했습니다.", Toast.LENGTH_SHORT).show()
                    }
                })

                libraryDetailViewModel.rateUpdateState.observe(viewLifecycleOwner, Observer { state ->
                    if (state == -1F) {
                        Toast.makeText(activity, "평점 등록 실패", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "평점 등록 되었습니다.", Toast.LENGTH_SHORT).show()
                    }
                })

                libraryDetailViewModel.goNextState.observe(viewLifecycleOwner, Observer { state ->
                    when(state) {
                        0 -> {

                        }

                        1 -> {

                        }

                        2 -> Toast.makeText(activity, "책 상태 변경 실패", Toast.LENGTH_SHORT).show()
                    }
                })

//                ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
//                    Log.d("test", "rating바 변화 감지!!")
//                    showPopup(rating, libraryId)
//                }

                readDoneButton.setOnClickListener {
                    libraryDetailViewModel.updateBookStatus(libraryId, 2, ratingBar.rating, 2)
                }

                goReadModeButton.setOnClickListener {
                    showPopup2()
                }

                listenButton.setOnClickListener {
                    val activity = requireActivity()
                    with (activity) {
                        val intent = Intent(this, PlayerActivity::class.java)
                        intent.putExtra("bookId", bookId)
                        startActivity(intent)
                    }
                }

                readButton.setOnClickListener {
                    mainActivity.goTimerActivity(bookId, bookImgUrl, libraryId)
                }

                homeDetailBackButton.setOnClickListener {
                    homeFragment.goMainFragment()
                }

            }

//            binding.ratingBar.rating = CustomApplication.getBookStars().toFloat()
//            binding.ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
//                // 평점을 바꾸시겠습니까?
//                showPopup(fl.toInt(), libraryId)
//            }

//            binding.readDoneButton.setOnClickListener {
//                // 요청 보내기
//                val data = UpdateBookStatusRequest(libraryId, 2, binding.ratingBar.rating.toInt())
//                RetrofitClient.api.updateBookStatus(data).enqueue(object: Callback<UpdateBookStatusResponse> {
//                    override fun onResponse(
//                        call: Call<UpdateBookStatusResponse>,
//                        response: Response<UpdateBookStatusResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            Toast.makeText(activity, "다 읽은 책으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Log.d("test", "#${response.code()}")
//                        }
//                    }
//
//                    override fun onFailure(call: Call<UpdateBookStatusResponse>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//                })
//            }
//            binding.goReadModeButton.setOnClickListener {
//                // readmodefragment로 replace 똑같은 Home fragment를 부모프래그먼트로 가지고 있음
//                // 책 id도 보내줘야함
//                showPopup2()
//            }
        }

//        libraryDetailViewModel.commentList.observe()

//        binding.homeDetailBackButton.setOnClickListener {
//            homeFragment.goMainFragment()
//        }
    }

//    fun setView(body: BookDetailResponse) {
//        Glide.with(binding.detailBookImageView).load(body.img_url)
//            .into(binding.detailBookImageView)
//        binding.descriptionTextView.text = body.description
//        binding.detailBookTitleTextView.text = body.title
//
//    }

    override fun onResume() {
        super.onResume()
        binding.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            Log.d("test", "rating바 변화 감지!!")
            if (fromUser) {
                showPopup(rating, libraryId)
            }
        }
    }

    private fun showPopup(rating: Float, libraryId: Int) {
        val view = layoutInflater.inflate(R.layout.alert_popup, null)

        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("평점을 등록하시겠습니까?")
            .setPositiveButton("확인") {dialog, which ->
                val bookStatus = CustomApplication.getBookStatus()
//                val requestBody = UpdateBookStatusRequest(libraryId, bookStatus, rating)
                libraryDetailViewModel.updateBookStatus(libraryId, bookStatus, rating, 3)
//                RetrofitClient.api.updateBookStatus(requestBody).enqueue(object: Callback<UpdateBookStatusResponse> {
//                    override fun onResponse(
//                        call: Call<UpdateBookStatusResponse>,
//                        response: Response<UpdateBookStatusResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            CustomApplication.setBookStars(binding.ratingBar.rating.toInt())
//                            Toast.makeText(activity, "평점이 등록되었습니다.", Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(activity, "등록 실패", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<UpdateBookStatusResponse>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//                })
            }
            .setNeutralButton("취소") {dialog, which ->
                binding.ratingBar.rating = preRating
            }
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }

    private fun showPopup2() {
        val view = layoutInflater.inflate(R.layout.alert_popup, null)
        val mList = arrayOf<String>("종이책", "오디오")
        val alertDialog = AlertDialog.Builder(activity)
            .setItems(mList, DialogInterface.OnClickListener { dialogInterface, i ->
//                val data = UpdateBookStatusRequest(libraryId, 1, binding.ratingBar.rating.toInt())
                libraryDetailViewModel.updateBookStatus(libraryId, 1, binding.ratingBar.rating, i)
//                RetrofitClient.api.updateBookStatus(data).enqueue(object: Callback<UpdateBookStatusResponse> {
//                    override fun onResponse(
//                        call: Call<UpdateBookStatusResponse>,
//                        response: Response<UpdateBookStatusResponse>
//                    ) {
//                        when (i) {
//                            0 -> {
//                                mainActivity.goTimerActivity(bookId, bookImgUrl, libraryId)
//                            }
//
//                            1 -> {
//                                mainActivity.goCameraActivity(bookId, libraryId)
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<UpdateBookStatusResponse>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//                })
            })
            .setTitle("독서 방식을 선택해주세요.")
            .create()

        alertDialog.setView(view)
        alertDialog.show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

}