package com.ssafy.herehear.model.network

import com.google.gson.GsonBuilder
import com.ssafy.herehear.model.network.api.RetrofitInterfaceAI
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClientAI {
    var api: RetrofitInterfaceAI
//    var gson = GsonBuilder().setLenient().create()
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/apis/")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(RetrofitInterfaceAI::class.java)
    }
}