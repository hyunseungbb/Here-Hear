from django.urls import path
from . import views

app_name = 'apis'
urlpatterns = [
    path('upload/', views.upload, name='upload'),
    path('ocr_tts/', views.ocr_tts, name='ocr_tts'),
]