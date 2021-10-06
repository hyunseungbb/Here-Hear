package com.ssafy.herehear.model.network.api

import com.ssafy.herehear.model.network.response.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterfaceRecommend {
    @GET("recommend")
    fun getRecommend(
        @Query("username") username: String
    ): Call<SearchResponse>
}