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

class IntroActivity : AppCompatActivity() {

    val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    private lateinit var getResultText: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val token = HereHear.prefs.getString("access_token", null)

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