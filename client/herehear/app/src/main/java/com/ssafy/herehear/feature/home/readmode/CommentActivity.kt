package com.ssafy.herehear.feature.home.readmode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.databinding.ActivityCommentBinding
import com.ssafy.herehear.data.network.RetrofitClient
import com.ssafy.herehear.data.network.response.CreateCommentRequest
import com.ssafy.herehear.data.network.response.CreateCommentResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommentActivity : AppCompatActivity() {
    val binding by lazy { ActivityCommentBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.commentButton.setOnClickListener {
            commentRequest()
        }
    }

    private fun commentRequest() {
        val time = intent.getIntExtra("time", 0)
        val bookId = intent.getIntExtra("bookId", 0)
        val libraryId = intent.getIntExtra("libraryId", 0)
        val comment = binding.commentText.text.toString()
        val body = CreateCommentRequest(comment, true, time)

        val url = "comment/${bookId}"
        val mainIntent = Intent(this, MainActivity::class.java)

        RetrofitClient.api.createComment(url, body).enqueue(object: Callback<CreateCommentResponse> {
            override fun onResponse(
                call: Call<CreateCommentResponse>,
                response: Response<CreateCommentResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "등록 완료", Toast.LENGTH_SHORT).show()
                    startActivity(mainIntent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "등록 실패", Toast.LENGTH_SHORT).show()
                    startActivity(mainIntent)
                    finish()
                }
            }

            override fun onFailure(call: Call<CreateCommentResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}