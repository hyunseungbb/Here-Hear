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
                    Log.d("test", "???????????? ?????? ????????????.")
                    good.setColorFilter(Color.parseColor("#6264A7"))
                } else {
                    Log.d("test", "???????????? ?????? ????????????.")
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
//                            // book dao??? ???????????? ??????
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
//                            // ??? ????????? ????????? ????????? ?????? true : ??????, false : ?????????
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

            // ??? ????????? ??? ??????/??????
//            binding.good.setOnClickListener {
//                registerBookClick(bookId, libraryId, flag)
//            }


//        }

    }


    // ????????? ??????
    fun setView(body: SearchDetailResponse) {
        // ??? ?????? ?????????
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

    // ?????? ????????? ??????
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
//                    // recycler adapter??? ?????? ?????????
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

    // ??? ????????? ????????? ????????? ???????????? ?????? flag - true:?????? , false:?????????
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
//                                Log.d("?????? ??? libraryId", "${libraryId}")
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

    // ??? ????????? ?????? ???????????? ??????
//    fun registerBookClick(bookId: Int, libraryId: Int, isRegistry: Boolean) {
//        // ???????????? ????????? ???
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
//                        Log.d("?????????", "??????")
//                        setLibraryId(bookId)
//                        Toast.makeText(mContext, "????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show()
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
//                        Log.d("?????????", "??????")
//                        Toast.makeText(mContext, "???????????? ?????? ?????????????????????.", Toast.LENGTH_SHORT).show()
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

//    // ???????????? ???????????? ?????? ???????????? libraryId??? ???????????? ?????? ???????????? ??????
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
//                                Log.d("?????? ??? libraryId", "${libraryId}")
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