from django.shortcuts import render, redirect
from gtts import gTTS
import json
import cv2
import requests
import sys
from django.views.decorators.http import require_POST
from .models import Article
from .forms import ArticleForm
import os
from django.core.files.storage import default_storage
from django.core.files.base import ContentFile
from django.conf import settings

# dotenv 처리해서 보안 높이기
appkey = '6ca73d4487a205586f13f1aebfe2f18a'

def test(image_path):
    
    OCR_URL = 'https://dapi.kakao.com/v2/vision/text/ocr'
    headers = {'Authorization': 'KakaoAK {}'.format(appkey)}

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


# # Create your views here.
# @require_POST
# def ocr(request):
#     # POST일 때
#     if request.method == 'POST':
#         form = ArticleForm(request.POST, request.FILES)
#         # form = ArticleForm(data=request.POST, files=request.FILES)
#         if form.is_valid():
#             article = form.save()
#             return redirect('articles:detail', article.pk)


def upload(request):
    context = {'request':request}
    return render(request, 'apis/upload.html', context)


def ocr(request):
    # print('-----ocr tts 시작-----')
    # print(request.FILES)

    # print('-----imgs에 바로 읽기 > 알단 후퇴-----')
    imgs = request.FILES.get('imgs').file
    # image = cv2.imread(imgs)
    # print(image)

    print('-----디스크에 저장-----')
    img_path = default_storage.save(f'tmp/{request.user}.jpg', ContentFile(imgs.read()))
    img_path2 = os.path.join(settings.MEDIA_ROOT, img_path)

    print('-----ocr tts-----')
    ocr_result, tts_result = test(img_path2)
    img_path3 = default_storage.delete(img_path)

    print('-----스피치 파일 저장하기-----')
    tts_path = f'/tmp/{request.user}.mp3'
    tts_result.save(f'{settings.MEDIA_ROOT}tts_path')
    
    context = {
        'ocr_result':ocr_result,
        'tts_result_url':'media'+tts_path
    }
    
    return render(request, 'apis/ocr.html', context)


def tts(request):
    return render(request, 'apis/tts.html')