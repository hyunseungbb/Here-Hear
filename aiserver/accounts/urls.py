from django.urls import path
from . import views

app_name = 'accounts'
urlpatterns = [
    path('recommend/', views.recommend, name='recommend'),
]