package com.ssafy.herehear.model.network.api

import com.ssafy.herehear.model.network.response.OCRTTSResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File

interface RetrofitInterfaceAI {
    @Multipart
    @POST("ocr_tts/")
    fun downloadAudio(
        @Part file: MultipartBody.Part
    ): Call<OCRTTSResponse>
}
