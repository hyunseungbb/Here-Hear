package com.ssafy.herehear.data.network.response


data class OCRTTSResponse(
    var ocr_result: String,
    var tts_file_name: String
)