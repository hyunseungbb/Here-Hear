from django.http.response import FileResponse
from gtts import gTTS
import json
import cv2
import requests
import sys
import os
from django.views.decorators.http import require_POST, require_GET
from django.core.files.storage import FileSystemStorage, default_storage
from django.core.files.base import ContentFile
from django.conf import settings
from django.shortcuts import render, redirect
from django.http import JsonResponse, HttpResponse
import mimetypes
# from .tts.test import naive_tts_run
# dotenv 처리해서 보안 높이기
# APPKEY = os.environ.get("APPKEY")
APPKEY = '6ca73d4487a205586f13f1aebfe2f18a'
LIMIT_PX = 1024
LIMIT_BYTE = 1024*1024  # 1MB
LIMIT_BOX = 40
def test(image_path):

    OCR_URL = 'https://dapi.kakao.com/v2/vision/text/ocr'
    headers = {'Authorization': 'KakaoAK {}'.format(APPKEY)}

    image = cv2.imread(image_path)
    height, width, _ = image.shape
    if LIMIT_PX < height or LIMIT_PX < width:
        ratio = float(LIMIT_PX) / max(height, width)
        image = cv2.resize(image, None, fx=ratio, fy=ratio)

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
def ocr_tts(request, username):
    # 폼으로 받은 데이터에 접근

    imgs = request.FILES.get('imgs').file

    # 바로 읽기 : 일단 후퇴
    # image = cv2.imread(imgs)
    # print(image)

    # 디스크에 저장
    img_path = default_storage.save(f'tmp/{username}.jpg', ContentFile(imgs.read()))
    img_path2 = os.path.join(settings.MEDIA_ROOT, img_path)

    # ocr, tts
    ocr_result, tts_result = test(img_path2)
    img_path3 = default_storage.delete(img_path)    # ocr 파일은 불필요하므로 삭제

    # 스피치 파일 저장하기

    tts_path = f'/tmp/{username}.mp3'
    tts_result.save(f'{settings.MEDIA_ROOT}{tts_path}')
    f'{settings.MEDIA_ROOT}{tts_path}'
    # json으로 넘겨줄 것
    context = {
        'ocr_result':ocr_result,
        # 'tts_result_url':'media'+tts_path
        'tts_file_name': f'{username}.mp3'
    }
    
    file_path = f'{settings.MEDIA_ROOT}{tts_path}'
    fs = FileSystemStorage(file_path)
    response = FileResponse(fs.open(file_path, 'rb'))
    response['Content-Disposition'] = f'attachment; filename={username}.mp3'

    # 장고 테스트
    # return render(request, 'apis/ocr.html', context)
    # rest api 서버
    return JsonResponse(context)
    # return response

# @require_GET
# def naive_tts(request):
#     naive_tts_run('정말로 사랑한담 기다려주세요')
#     file_path = f'./tts/output/tmp'
#     fs = FileSystemStorage(file_path)
#     response = FileResponse(fs.open(file_path, 'rb'))
#     response['Content-Disposition'] = f'attachment; filename=test.wav'
#     # file = open(file_path, 'r', encoding='cp949')
#     # mime_type, _ = mimetypes.guess_type(file_path)
#     # response = HttpResponse(file, content_type=mime_type)
#     # response['Content-Disposition'] = f'attachment; filename={username}.mp3'
#     return response


@require_GET
def download(request, username):
    print(username)
    file_path = f'{settings.MEDIA_ROOT}/tmp/{username}.mp3'
    fs = FileSystemStorage(file_path)
    response = FileResponse(fs.open(file_path, 'rb'))
    response['Content-Disposition'] = f'attachment; filename={username}.mp3'
    # file = open(file_path, 'r', encoding='cp949')
    # mime_type, _ = mimetypes.guess_type(file_path)
    # response = HttpResponse(file, content_type=mime_type)
    # response['Content-Disposition'] = f'attachment; filename={username}.mp3'
    return response
