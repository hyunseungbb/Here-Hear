from django.db import models

class Accounts(models.Model):
    name = models.CharField(max_length=10)
    password = models.CharField(max_length=10)

class Books(models.Model):
    name = models.CharField(max_length=20)
    isbn13 = models.CharField(max_length=13)

class Libraries(models.Model):
    account_id = models.ForeignKey(Accounts, on_delete=models.CASCADE)
    book_id = models.ForeignKey(Books, on_delete=models.CASCADE)
    stars = models.FloatField()