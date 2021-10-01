package com.ssafy.herehear.model.network

import com.ssafy.herehear.HereHear
import com.ssafy.herehear.model.network.api.RetrofitInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


object RetrofitClient {

    var api: RetrofitInterface

    init {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8081/api/v1/")
            .client(provideOkHttpClient(AppInterceptor()))
            .addConverterFactory(GsonConverterFactory.create())
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
            val newRequest = request().newBuilder()
                .addHeader("Authorization", HereHear.prefs.getString("access_token", null))
                .build()

            proceed(newRequest)
        }
    }
}