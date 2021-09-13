from django.shortcuts import render

# Create your views here.
def ocr(request):
    return render(request, 'ocr.html')

def tts(request):
    return render(request, 'tts.html')