package com.ssafy.herehear.feature.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.ssafy.herehear.HereHear
import com.ssafy.herehear.IntroActivity
import com.ssafy.herehear.MainActivity
import com.ssafy.herehear.databinding.ActivitySignupBinding

import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.LoginRequest
import com.ssafy.herehear.model.network.response.LoginResponse
import com.ssafy.herehear.model.network.response.SignupRequest
import com.ssafy.herehear.model.network.response.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
//
//        val userId = binding.userId
//        val password = binding.password
//        val login = binding.loginButton2
//        val loading = binding.loading

        var introIntent = Intent(this, IntroActivity::class.java)
        binding.signupButton2.setOnClickListener {
            val userId = binding.userId?.text.toString()
            val userPassword = binding.userPassword?.text.toString()
            val userPasswordConfirmation = binding.userPassword2?.text.toString()
            if (userPassword != userPasswordConfirmation) {
                Toast.makeText(applicationContext, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
            } else {
                val signupData = SignupRequest(userPassword, userId)
                RetrofitClient.api.signup(signupData).enqueue(object: Callback<SignupResponse> {
                    override fun onResponse(
                        call: Call<SignupResponse>,
                        response: Response<SignupResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(applicationContext, "회원가입 성공", Toast.LENGTH_SHORT).show()
                            startActivity(introIntent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                        Log.d("test", "회원가입 실패!!")
                    }

                })
            }


        }
        binding.signupBackButton?.setOnClickListener {
            val signupBackIntent = Intent(this, IntroActivity::class.java)
            startActivity(signupBackIntent)
            finish()
        }
//        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
//            .get(LoginViewModel::class.java)
//
//        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
//            val loginState = it ?: return@Observer
//
//            // disable login button unless both username / password is valid
//            login.isEnabled = loginState.isDataValid
//
//            if (loginState.usernameError != null) {
//                username.error = getString(loginState.usernameError)
//            }
//            if (loginState.passwordError != null) {
//                password.error = getString(loginState.passwordError)
//            }
//        })
//
//        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
//            val loginResult = it ?: return@Observer
//
//            loading.visibility = View.GONE
//            if (loginResult.error != null) {
//                showLoginFailed(loginResult.error)
//            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
//            setResult(Activity.RESULT_OK)
//
//            //Complete and destroy login activity once successful
//            finish()
//        })

//        username.afterTextChanged {
//            loginViewModel.loginDataChanged(
//                username.text.toString(),
//                password.text.toString()
//            )
//        }
//
//        password.apply {
//            afterTextChanged {
//                loginViewModel.loginDataChanged(
//                    username.text.toString(),
//                    password.text.toString()
//                )
//            }
//
//            setOnEditorActionListener { _, actionId, _ ->
//                when (actionId) {
//                    EditorInfo.IME_ACTION_DONE ->
//                        loginViewModel.login(
//                            username.text.toString(),
//                            password.text.toString()
//                        )
//                }
//                false
//            }
//
//            login.setOnClickListener {
//                loading.visibility = View.VISIBLE
//                loginViewModel.login(username.text.toString(), password.text.toString())
//            }
//        }
    }
//
//    private fun updateUiWithUser(model: LoggedInUserView) {
//        val welcome = getString(R.string.welcome)
//        val displayName = model.displayName
//        // TODO : initiate successful logged in experience
//        Toast.makeText(
//            applicationContext,
//            "$welcome $displayName",
//            Toast.LENGTH_LONG
//        ).show()
//    }
//
//    private fun showLoginFailed(@StringRes errorString: Int) {
//        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
//    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
//}