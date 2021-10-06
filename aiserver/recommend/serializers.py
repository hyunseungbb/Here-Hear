from rest_framework import serializers
from .models import Account, Book, Comment, Library

class AccountSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = Account


class BookSerializer(serializers.ModelSerializer):

    class Meta:
        model = Book
        fields = '__all__'


class CommentSerializer(serializers.ModelSerializer):

    class Meta:
        model = Comment
        

class LibrarySerializer(serializers.ModelSerializer):

    class Meta:
        model = Library