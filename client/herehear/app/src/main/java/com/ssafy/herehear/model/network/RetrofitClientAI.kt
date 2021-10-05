package com.ssafy.herehear.model.network

import com.google.gson.GsonBuilder
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.model.network.api.RetrofitInterfaceAI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
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
//            .baseUrl("http://10.0.2.2:8000/apis/")
            .baseUrl("http://192.168.35.188:8000/apis/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitInterfaceAI::class.java)
    }

}