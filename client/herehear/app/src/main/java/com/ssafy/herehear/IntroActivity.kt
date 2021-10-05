package com.ssafy.herehear

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ssafy.herehear.databinding.ActivityIntroBinding
import com.ssafy.herehear.feature.login.ui.login.LoginActivity
import com.ssafy.herehear.feature.signup.SignupActivity
import com.ssafy.herehear.model.Preference
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.LoginRequest
import com.ssafy.herehear.model.network.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IntroActivity : AppCompatActivity() {

    val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    private lateinit var getResultText: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val token = HereHear.prefs.getString("access_token", null)
        val savedUserId: String = HereHear.prefs.getString("userId", "")
        val savedUserPassword: String = HereHear.prefs.getString("userPassword", "")
        val mainIntent = Intent(this, MainActivity::class.java)
        if (savedUserId != "" && savedUserPassword != "") {
            val loginData = LoginRequest(savedUserId, savedUserPassword)
            RetrofitClient.api.login(loginData).enqueue(object: Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "자동로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                        var token = response.body()?.accessToken
                        HereHear.prefs.setString("access_token", token)
                        startActivity(mainIntent)
                        finish()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
//        if (token != "null") {
//            Toast.makeText(applicationContext, "자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show()
//            intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
        getResultText = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val message = result.data?.getIntExtra("bookId", 0)
            }
        }

        binding.loginButton1.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
//            getResultText.launch(loginIntent)
        }
//
        binding.signupButton.setOnClickListener {
            val signupIntent = Intent(this, SignupActivity::class.java)
//            getResultText.launch(signupIntent)
            startActivity(signupIntent)
            finish()
        }

//        var handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            var intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }, 1000)

    }
}