from django.contrib import admin
from django.urls import path, include
from django.conf import settings
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('apis/', include('apis.urls')),
    path('api/v1/recommend', include('recommend.urls')),
] + static(settings.MEDIA_URL, document_root=settings.MEDIA_ROOT)
