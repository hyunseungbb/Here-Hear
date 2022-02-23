package com.ssafy.herehear.data.network

import com.ssafy.herehear.CustomApplication
import com.ssafy.herehear.data.network.api.RetrofitInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitClient {

    var api: RetrofitInterface
    val retrofit: Retrofit

    init {

        retrofit = Retrofit.Builder()
//            .baseUrl("http://j5b105.p.ssafy.io:8081/api/v1/")
            .baseUrl("http://10.0.2.2:8001/api/v1/")
//            .baseUrl("http://192.168.35.41:8081/api/v1/")
            .client(provideOkHttpClient(AppInterceptor()))
//            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        api = retrofit.create(RetrofitInterface::class.java)
    }

    private fun provideOkHttpClient(
        interceptor: AppInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    class AppInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
            val token = "Bearer " + CustomApplication.prefs.getString("access_token", "")
            val newRequest = request().newBuilder()
                .addHeader("Authorization", token)
                .build()

            proceed(newRequest)
        }
    }
}