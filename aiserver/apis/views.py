from django.shortcuts import render
from gtts import gTTS
import json
import cv2
import requests
import sys

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

    tts = gTTS(text=text, lang='ko')


# Create your views here.
def ocr(request):

    return render(request, 'apis/ocr.html')




def tts(request):
    return render(request, 'apis/tts.html')