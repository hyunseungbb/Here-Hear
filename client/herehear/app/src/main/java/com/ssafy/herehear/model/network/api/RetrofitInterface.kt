package com.ssafy.herehear.model.network.api

import com.ssafy.herehear.model.network.response.LoginRequest
import com.ssafy.herehear.model.network.response.LoginResponse
import com.ssafy.herehear.model.network.response.SearchRequest
import com.ssafy.herehear.model.network.response.SearchResponse
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @POST("login")
    @Headers("Content-type: application/json")
    fun login(
        @Body loginbody: LoginRequest
    ): Call<LoginResponse>

    @GET("search")
    @Headers("Content-type: application/json")
    fun search(
        @Query("searchText") searchText: String,
        @Query("searchNo") searchNo: Int
    ): Call<SearchResponse>

}
