from django.shortcuts import render, get_list_or_404, get_object_or_404
from django.http.response import JsonResponse

from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_framework import status

from .models import Account, Library

# Create your views here.
@api_view(['GET'])
def recommend(request):
    print(request.data.get('username'))
    if request.method == 'GET':
        accounts = Account.objects.all()
        account_json = []

        for account in accounts:
            # if account.username == username:
                account_json.append(
                    {
                        'id': account.pk,
                        'username': account.username,
                        'authority': account.authority,
                    }
                )

        return JsonResponse(account_json, safe=False)


