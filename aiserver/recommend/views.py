import pandas as pd
import numpy as np
from sklearn.metrics import mean_squared_error

from django.shortcuts import render, get_list_or_404, get_object_or_404
from django.http.response import JsonResponse

from rest_framework.response import Response
from rest_framework.decorators import api_view
from rest_framework import status

from .serializers import BookSerializer
from .models import Account, Library, Book


# 알고리즘 평가하기 위한 mse metric
def get_rmse(R, P, Q, non_zeros):
    error = 0
    full_pred_matrix = np.dot(P, Q.T)
    x_non_zero_ind = [non_zero[0] for non_zero in non_zeros]
    y_non_zero_ind = [non_zero[1] for non_zero  in non_zeros]
    R_non_zeros = R[x_non_zero_ind, y_non_zero_ind]
    full_pred_matrix_non_zeros = full_pred_matrix[x_non_zero_ind, y_non_zero_ind]
    mse = mean_squared_error(R_non_zeros, full_pred_matrix_non_zeros)
    rmse = np.sqrt(mse)
    return rmse


@api_view(['GET'])
def recommend(request):
    username = request.GET.get('username', None)
    if request.method == 'GET':
        account = get_object_or_404(Account, username=username)

        # 데이터 변환
        libraries = pd.DataFrame(list(Library.objects.all().values()))
        # 필요 컬럼 추출
        col = ['user_id', 'book_id', 'stars']

        # 추천 알고리즘 구현을 위한 pivot table 변환
        try:
            rating_matrix = libraries.pivot_table('stars', index='user_id', columns='book_id').fillna(0)
        except:
            return JsonResponse([{}], safe=False)
        R = rating_matrix.values

        K = 20                  # 잠재요인 차원
        steps = 100             # SGD 횟수
        learning_rate = 0.01
        r_lambda = 0.01         # lasso 규제 적용

        # libraries에 등록된 사용자, 도서 수
        num_users, num_items = R.shape

        # 사용자 평점 데이터 R에서 필요한 데이터만 추출한 후 이 행렬을 행렬 P, Q로 분해
        # P, Q 행렬 랜덤하게 초기 생성
        P = np.random.normal(scale=1./K, size=(num_users, K))
        Q = np.random.normal(scale=1./K, size=(num_items, K))

        prev_rmse = 10000
        break_count = 0

        # 사용자 평점 데이터 R에서 non zero인 요소만 추려내는 과정
        non_zeros = [ (i,j,R[i,j]) for i in range(num_users) for j in range(num_items) if R[i,j]>0]

        # 데이터가 연속형(continuous)이므로 metric으로 mean_squared_error 사용

        # 역전파를 응용해 P, Q를 업데이트
        for step in range(steps):
            for i,j,r in non_zeros:
                eij = r -np.dot(P[i,:], Q[j,:].T)
                P[i,:] = P[i,:] + learning_rate*(eij * Q[j,:] - r_lambda * P[i,:])
                Q[j,:] = Q[j,:] + learning_rate*(eij * P[i,:] - r_lambda * Q[j,:])

            rmse = get_rmse(R,P,Q,non_zeros)
            if (step % 10) == 0:
                print(step, rmse)
        
        # 우리가 예측한 평점데이터
        pred_matrix = np.dot(P, Q.T)

        # AI를 활용하기 좋은 데이터 타입인 dataframe 자료형으로 변환하겠습니다
        ratings_pred_matrix = pd.DataFrame(data=pred_matrix, index=rating_matrix.index, columns=rating_matrix.columns)

        # 이미 본 책을 제외하고 유저가 좋아할만한 책 10권 추출
        top_n = 3
        user_rating = rating_matrix.loc[account.id, :]
        already_seen = user_rating[user_rating>0].index.tolist()
        books_list = rating_matrix.columns.tolist()
        unseen_list = [book for book in books_list if book not in already_seen]
        recom_books = ratings_pred_matrix.loc[account.id, unseen_list].sort_values(ascending=False)[:top_n]

        print(ratings_pred_matrix)
        print(recom_books)
        res = []
        for rec_id in list(recom_books.index):
            book = get_object_or_404(Book, pk=rec_id)
            serializer = BookSerializer(book)

            res.append(
                serializer.data
            )

        return JsonResponse(res, safe=False)


