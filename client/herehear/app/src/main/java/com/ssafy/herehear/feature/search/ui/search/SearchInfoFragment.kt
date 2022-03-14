package com.ssafy.herehear.feature.search.ui.search

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.ssafy.herehear.data.local.dao.BookDao
import com.ssafy.herehear.data.local.database.DatabaseManager
import com.ssafy.herehear.data.local.entity.Book
import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.databinding.FragmentSearchInfoBinding
import com.ssafy.herehear.feature.search.SearchFragment
import com.ssafy.herehear.feature.search.adapater.SearchDetailAdapter
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.response.*
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class SearchInfoFragment : Fragment() {

    lateinit var mContext: Context
    private lateinit var inflaterr: LayoutInflater
    private lateinit var binding: FragmentSearchInfoBinding
    private var flag = false
    private var libraryId = 0
    private val searchViewModel: SearchViewModel by viewModels({requireParentFragment()})
    private lateinit var commentAdapter: SearchDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel.getSearchDetail()
        searchViewModel.getComments()
        searchViewModel.isBookInMyLibrary()

        with(binding) {

            with(bookCommentRecycler) {
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = SearchDetailAdapter().also {
                    commentAdapter = it
                }
            }

            good.setOnClickListener {
                val isRegistered = searchViewModel.isLibraryRegisterd.value ?: return@setOnClickListener
                if (!isRegistered) {
                    searchViewModel.addLibrary()
                } else {
                    searchViewModel.deleteLibrary()
                }
            }

            searchInfoBack.setOnClickListener {
                (parentFragment as SearchFragment).goMainFragment()
            }

            searchViewModel.bookDetail.observe(viewLifecycleOwner, Observer { searchDetailResponse ->
                setView(searchDetailResponse)
            })

            searchViewModel.isLibraryRegisterd.observe(viewLifecycleOwner, Observer { like ->
                if (like) {
                    Log.d("test", "내서재에 있는 책입니다.")
                    good.setColorFilter(Color.parseColor("#6264A7"))
                } else {
                    Log.d("test", "내서재에 없는 책입니다.")
                    good.setColorFilter(Color.parseColor("#000000"))
                }
            })

            searchViewModel.comment.observe(viewLifecycleOwner, Observer { commentList ->
                commentAdapter.updateData(commentList)
            })

            searchViewModel.bookDetail.observe(viewLifecycleOwner, Observer { book ->
                setView(book)
            })
        }

//        setFragmentResultListener("request") { key, bundle ->
//            val bookId = bundle.getInt("valueKey")
//
//            var url = "books/${bookId}"
//            RetrofitClient.api.getSearchDetail(url)
//                .enqueue(object : Callback<SearchDetailResponse> {
//                    override fun onResponse(
//                        call: Call<SearchDetailResponse>,
//                        response: Response<SearchDetailResponse>
//                    ) {
//                        if (response.isSuccessful) {
//                            var body = response.body()
//                            if (body != null) {
//                                setView(body)
//                                getComments(body.id)
//                            }
//
//                            // book dao에 만들어서 넣자
//                            val book = Book(body!!.id, body!!.description, body!!.img_url, body!!.title)
//                            val bookList = mutableListOf<Book>(book)
//                            val databaseManager = Room.databaseBuilder(
//                                activity!!.applicationContext,
//                                DatabaseManager::class.java,
//                                "database"
//                            ).fallbackToDestructiveMigration()
//                                .allowMainThreadQueries()
//                                .build()
//                            databaseManager.bookDao.insertBook(bookList)
////                            BookDao.insertBook(book)
//
//                            // 내 서재에 등록된 책인지 체크 true : 등록, false : 미등록
//                            getMyLibraryBooks(bookId)
//
//                        } else {
//                            Log.d("response", "response is null")
//                        }
//
//                    }
//
//                    override fun onFailure(call: Call<SearchDetailResponse>, t: Throwable) {
//                        t.printStackTrace()
//                    }
//
//                })

            // 내 서재에 책 등록/삭제
//            binding.good.setOnClickListener {
//                registerBookClick(bookId, libraryId, flag)
//            }


//        }

    }


    // 렌더링 함수
    fun setView(body: SearchDetailResponse) {
        // 책 표지 렌더링
        Glide.with(binding.bookCover).load(body.img_url).into(binding.bookCover)

        binding.bookTitle.text = body.title
        binding.bookDescription.text = body.description

        var stars = 0
        if (body.stars_count != 0){
            stars = body.stars_sum / body.stars_count
        }
        when (stars) {
            0 -> {
            }
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


    }

    // 댓글 렌더링 함수
//    fun getComments(bookId: Long) {
//        var url = "comment/${bookId}"
//        RetrofitClient.api.getAllComments(url).enqueue(object : Callback<AllCommentsResponse> {
//            override fun onResponse(
//                call: Call<AllCommentsResponse>,
//                response: Response<AllCommentsResponse>
//            ) {
//                if (response.isSuccessful) {
//                    var bookData = mutableListOf<AllCommentsResponseItem>()
//                    var body = response.body()
//
//                    if (body != null) {
//                        for (item in body) {
//                            bookData.add(item)
//                        }
//                    }
//
//                    // recycler adapter를 통한 바인딩
//                    var recyclerAdapter = SearchDetailAdapter()
//                    recyclerAdapter.listData = bookData
//                    binding.bookCommentRecycler.adapter = recyclerAdapter
//                    binding.bookCommentRecycler.layoutManager =
//                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//                }
//            }
//
//            override fun onFailure(call: Call<AllCommentsResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//
//        })
//    }

    // 내 서재에 등록된 책인지 확인하는 함수 flag - true:등록 , false:미등록
//    fun getMyLibraryBooks(bookId: Int) {
//        var result = false
//        RetrofitClient.api.getMyLibrary2().enqueue(object : Callback<GetMyLibraryResponse> {
//            override fun onResponse(
//                call: Call<GetMyLibraryResponse>,
//                response: Response<GetMyLibraryResponse>
//            ) {
//                if (response.isSuccessful) {
//                    var bookData = mutableListOf<GetMyLibraryResponseItem>()
//                    var body = response.body()
//
//                    if (body != null) {
//                        for (item in body) {
//                            bookData.add(item)
//                            if (bookId == item.book_id) {
//                                libraryId = item.id
//                                Log.d("함수 내 libraryId", "${libraryId}")
//                                result = true
//                            }
//                        }
//                        flag = result
//
//                        if (flag){
//                            binding.good.setColorFilter(Color.parseColor("#6264A7"))
//                        } else {
//                            binding.good.setColorFilter(Color.parseColor("#000000"))
//                        }
//
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<GetMyLibraryResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//
//        })
//    }

    // 내 서재에 책을 등록하는 함수
//    fun registerBookClick(bookId: Int, libraryId: Int, isRegistry: Boolean) {
//        // 좋아요를 눌렀을 때
//        val isRegistered = searchViewModel.isLibraryRegisterd.value ?: return
//        if (!isRegistered) {
//            searchViewModel.addLibrary()
//            var url = "libraries/${bookId}"
//            RetrofitClient.api.registerBook(url).enqueue(object : Callback<Library> {
//                override fun onResponse(
//                    call: Call<Library>,
//                    response: Response<Library>
//                ) {
//                    if (response.isSuccessful) {
//                        Log.d("책등록", "성공")
//                        setLibraryId(bookId)
//                        Toast.makeText(mContext, "서재에 책이 등록되었습니다.", Toast.LENGTH_SHORT).show()
//                        flag = true
//                        if (flag){
//                            binding.good.setColorFilter(Color.parseColor("#6264A7"))
//                        }
//                        val library: List<Library> = listOf(response.body()!!)
//                        val databaseManager = Room.databaseBuilder(
//                            activity!!.applicationContext,
//                            DatabaseManager::class.java,
//                            "database"
//                        ).fallbackToDestructiveMigration()
//                            .allowMainThreadQueries()
//                            .build()
//                        databaseManager.libraryDao.insertLibrarys(library)
//                    }
//                }
//
//                override fun onFailure(
//                    call: Call<Library>,
//                    t: Throwable
//                ) {
//                    t.printStackTrace()
//                }
//
//            })
//        } else {
//            searchViewModel.deleteLibrary()
//            var url = "libraries/${libraryId}"
//            RetrofitClient.api.deleteBook(url).enqueue(object : Callback<DeleteBookResponse> {
//                override fun onResponse(
//                    call: Call<DeleteBookResponse>,
//                    response: Response<DeleteBookResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        Log.d("책삭제", "성공")
//                        Toast.makeText(mContext, "서재에서 책을 제거하였습니다.", Toast.LENGTH_SHORT).show()
//                        flag = false
//                        if (!flag){
//                            binding.good.setColorFilter(Color.parseColor("#000000"))
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<DeleteBookResponse>, t: Throwable) {
//                    t.printStackTrace()
//                }
//
//
//            })
//        }
//
//    }

//    // 서재에서 삭제하고 다시 등록하면 libraryId가 갱신돼서 다시 찾아주는 함수
//    fun setLibraryId(bookId: Int) {
//        RetrofitClient.api.getMyLibrary2().enqueue(object : Callback<GetMyLibraryResponse> {
//            override fun onResponse(
//                call: Call<GetMyLibraryResponse>,
//                response: Response<GetMyLibraryResponse>
//            ) {
//                if (response.isSuccessful) {
//                    var bookData = mutableListOf<GetMyLibraryResponseItem>()
//                    var body = response.body()
//
//                    if (body != null) {
//                        for (item in body) {
//                            bookData.add(item)
//                            if (bookId == item.book_id) {
//                                libraryId = item.id
//                                Log.d("함수 내 libraryId", "${libraryId}")
//                            }
//                        }
//
//                    }
//
//                }
//            }
//
//            override fun onFailure(call: Call<GetMyLibraryResponse>, t: Throwable) {
//                t.printStackTrace()
//            }
//
//        })
//    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mContext = context
    }
}