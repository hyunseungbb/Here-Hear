package com.ssafy.herehear.model.network

import com.ssafy.herehear.model.network.api.RetrofitInterfaceRecommend
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClientRecommend {
    var api: RetrofitInterfaceRecommend
    //    var gson = GsonBuilder().setLenient().create()
    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://127.0.0.1:8000/api/v1/")
            .baseUrl("http://10.0.2.2:8000/api/v1/")
//            .baseUrl("http://192.168.35.41:8000/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitInterfaceRecommend::class.java)
    }
}