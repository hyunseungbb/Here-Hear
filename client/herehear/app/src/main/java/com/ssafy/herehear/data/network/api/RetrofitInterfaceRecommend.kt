package com.ssafy.herehear.data.network.api

import com.ssafy.herehear.data.network.response.SearchResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterfaceRecommend {
    @GET("recommend")
    fun getRecommend(
        @Query("username") username: String
    ): Single<SearchResponse>
}