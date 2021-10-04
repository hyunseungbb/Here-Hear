package com.ssafy.herehear.model.network.response


data class OCRTTSResponse(
    var ocr_result: String,
    var tts_file_name: String
)