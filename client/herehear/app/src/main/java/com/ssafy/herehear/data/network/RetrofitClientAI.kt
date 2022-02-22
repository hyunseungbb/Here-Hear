package com.ssafy.herehear.data.network

import com.ssafy.herehear.data.network.api.RetrofitInterfaceAI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClientAI {
    var api: RetrofitInterfaceAI
//    var gson = GsonBuilder().setLenient().create()
    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://j5b105.p.ssafy.io:8000/apis/")
            .baseUrl("http://10.0.2.2:8000/apis/")
//            .baseUrl("http://192.168.35.41:8000/apis/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitInterfaceAI::class.java)
    }

}