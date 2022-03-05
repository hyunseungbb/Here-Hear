package com.ssafy.herehear.data.network.api

import com.ssafy.herehear.data.local.entity.Library
import com.ssafy.herehear.data.network.response.*
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @POST("auth/login")
    @Headers("Content-type: application/json")
    fun login(
        @Body loginbody: LoginRequest
    ): Call<LoginResponse>

    @POST("auth/signup")
    fun signup(
        @Body signupBody: SignupRequest
    ): Call<SignupResponse>

    @GET("books/search")
    fun getSearch(
        @Query("type") type: String,
        @Query("keyword") keyword: String
    ): Call<SearchResponse>

    @GET
    fun getSearchDetail(@Url url: String): Call<SearchDetailResponse>

    @GET("libraries/mine")
    fun getMyLibrary(): Single<GetMyLibraryResponse>

    @GET("libraries/mine")
    fun getMyLibrary2(): Call<GetMyLibraryResponse>

    @GET
    fun getHomeBookDetail(@Url url: String): Call<BookDetailResponse>

    @GET
    fun getAllComments(@Url url: String): Single<AllCommentsResponse>

    @POST
    fun registerBook(
        @Url url: String,
    ): Call<Library>

    @DELETE
    fun deleteBook(
        @Url url: String
    ): Call<DeleteBookResponse>

    @PUT("libraries")
    fun updateBookStatus(
        @Body bookStatusBody: UpdateBookStatusRequest
    ): Single<UpdateBookStatusResponse>

    @POST
    fun createComment(
        @Url url: String,
        @Body commentBody: CreateCommentRequest
    ): Call<CreateCommentResponse>

    @GET("comment/my")
    fun getMyComments(): Call<MyCommentsResponse>
}

