from django.urls import path
from . import views

app_name = 'apis'
urlpatterns = [
    path('upload/', views.upload, name='upload'),
    path('ocr_tts/<username>/', views.ocr_tts, name='ocr_tts'),
    # path('ocr_tts/<string:username>/', views.ocr_tts, name='ocr_tts'),
    # path('naive_tts/', views.naive_tts, name='naive_tts'),
    path('download/<username>/', views.download, name='download'),
]