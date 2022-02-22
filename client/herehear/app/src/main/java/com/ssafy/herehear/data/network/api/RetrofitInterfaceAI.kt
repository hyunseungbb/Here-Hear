package com.ssafy.herehear.data.network.api

import com.ssafy.herehear.data.network.response.OCRTTSResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

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
