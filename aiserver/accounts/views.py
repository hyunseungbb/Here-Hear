from django.shortcuts import render

def recommend(request):
    return render(request, 'accounts/recommend.html')
