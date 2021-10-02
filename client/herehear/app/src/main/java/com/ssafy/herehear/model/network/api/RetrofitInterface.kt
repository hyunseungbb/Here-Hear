package com.ssafy.herehear.model.network.api

import com.ssafy.herehear.model.network.response.*
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

    @GET("search")
    @Headers("Content-type: application/json")
    fun search(
        @Query("searchText") searchText: String,
        @Query("searchNo") searchNo: Int
    ): Call<SearchResponse>

    @GET("libraries/mine")
    fun getMyLibrary(): Call<GetMyLibraryResponse>

    @GET("account/me")
    fun getMyAccountInfo(): Call<MyAccountInfoResponse>
}

