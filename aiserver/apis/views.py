from gtts import gTTS
import json
import cv2
import requests
import sys
import os
from django.views.decorators.http import require_POST
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.conf import settings
from django.shortcuts import render, redirect
from django.http import JsonResponse 

# dotenv 처리해서 보안 높이기
APPKEY = os.environ.get("APPKEY")


def test(image_path):
    
    OCR_URL = 'https://dapi.kakao.com/v2/vision/text/ocr'
    headers = {'Authorization': 'KakaoAK {}'.format(APPKEY)}

    image = cv2.imread(image_path)
    jpeg_image = cv2.imencode(".jpg", image)[1]

    ocr_response = requests.post(OCR_URL, headers=headers, files={"image": jpeg_image.tobytes()}).json()

    tmp_text = []
    for values in ocr_response['result']:
        tmp_text.extend(values['recognition_words'])

    text = ' '.join(tmp_text)
    print('-----ocr 결과 : ', text)

    tts = gTTS(text=text, lang='ko')
    return text, tts


# 장고 테스트용 프론트 (tmp)
def upload(request):
    context = {'request':request}
    return render(request, 'apis/upload.html', context)


@require_POST
def ocr_tts(request):
    # 폼으로 받은 데이터에 접근
    imgs = request.FILES.get('imgs').file

    # 바로 읽기 : 일단 후퇴
    # image = cv2.imread(imgs)
    # print(image)

    # 디스크에 저장
    img_path = default_storage.save(f'tmp/{request.user}.jpg', ContentFile(imgs.read()))
    img_path2 = os.path.join(settings.MEDIA_ROOT, img_path)

    # ocr, tts
    ocr_result, tts_result = test(img_path2)
    img_path3 = default_storage.delete(img_path)    # ocr 파일은 불필요하므로 삭제

    # 스피치 파일 저장하기
    tts_path = f'/tmp/{request.user}.mp3'
    tts_result.save(f'{settings.MEDIA_ROOT}tts_path')
    
    # json으로 넘겨줄 것
    context = {
        'ocr_result':ocr_result,
        'tts_result_url':'media'+tts_path
    }
    
    # 장고 테스트
    # return render(request, 'apis/ocr.html', context)
    # rest api 서버
    return JsonResponse(context)

