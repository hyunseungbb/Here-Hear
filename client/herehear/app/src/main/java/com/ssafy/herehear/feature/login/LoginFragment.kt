package com.ssafy.herehear.feature.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.herehear.R
import com.ssafy.herehear.databinding.FragmentLoginBinding
import com.ssafy.herehear.model.network.RetrofitClient
import com.ssafy.herehear.model.network.response.LoginRequest
import com.ssafy.herehear.model.network.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding:FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loginBtn.setOnClickListener {
            val id = binding.idForm.text.toString()
            val password = binding.passwordForm.text.toString()
            val loginData = LoginRequest(id, password)
            RetrofitClient.api.login(loginData).enqueue(object: Callback<LoginResponse>{
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("test", "성공쓰 ${response.body().toString()}")
                    var data = response.body()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Log.d("test1", "${t}")
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }
}