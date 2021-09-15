from django.urls import path
from . import views

app_name = 'apis'
urlpatterns = [
    path('upload/', views.upload, name='upload'),
    path('ocr/', views.ocr, name='ocr'),
    path('tts/', views.tts, name='tts')
]