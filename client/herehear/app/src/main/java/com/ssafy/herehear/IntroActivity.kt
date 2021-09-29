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
import com.ssafy.herehear.model.Preference

class IntroActivity : AppCompatActivity() {

    val binding by lazy { ActivityIntroBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        lateinit var intent : Intent
        val token = HereHear.prefs.getString("access_token", null)
        if (token != null || token != "") {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginButton1.setOnClickListener {
            intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
//
//        binding.signupButton.setOnClickListener {
//            intent = Intent(this, SignupActivity::class.java)
//            startActivity(intent)
//            finish()
//        }

//        var handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            var intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
//        }, 1000)

    }

    override fun onPause() {
        super.onPause()
        finish()
    }
}