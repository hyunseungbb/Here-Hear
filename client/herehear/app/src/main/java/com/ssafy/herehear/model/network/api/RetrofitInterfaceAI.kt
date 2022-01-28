package com.ssafy.herehear.model.network.api

import com.ssafy.herehear.model.network.response.OCRTTSResponse
import com.ssafy.herehear.model.network.response.SearchResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import java.io.File

interface RetrofitInterfaceAI {
    @Multipart
    @POST
    suspend fun downloadAudio(
        @Url url: String,
        @Part file: MultipartBody.Part
    ): Response<OCRTTSResponse>

    @Multipart
    @POST
    fun downloadAudio2(
        @Url url: String,
        @Part file: MultipartBody.Part
    ): Call<OCRTTSResponse>

    @GET
    fun downloadAudioFile(
        @Url url: String
    ): Call<ResponseBody>
}
