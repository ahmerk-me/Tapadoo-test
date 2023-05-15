package com.tapadootest.app.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cells.library.classes.*
import com.tapadootest.app.R
import com.tapadootest.app.classes.*
import com.tapadootest.app.databinding.ActivitySplashCustBinding
import com.tapadootest.app.viewmodel.UserAuthViewModel


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashCustBinding

    lateinit var userViewModel: UserAuthViewModel


    override fun attachBaseContext(newBase: Context?) {

        super.attachBaseContext(LocaleHelper.onAttach(newBase!!))

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivitySplashCustBinding.inflate(layoutInflater)

        val view: View = binding.root

        sessionManager = SessionManager(this)

        languageSessionManager = LanguageSessionManager(this)

//        sessionManager?.token = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJJZCI6IjdiMDFhZjI4LWVkNWUtNDY4OS05OGM3LWYwY2EwZTAyY2ZjNyIsImh0dHA6Ly9zY2hlbWFzLm1pY3Jvc29mdC5jb20vd3MvMjAwOC8wNi9pZGVudGl0eS9jbGFpbXMvcm9sZSI6IlZlbmRvciIsImV4cCI6MjEzNDE5MzkwMiwiaXNzIjoiaHR0cHM6Ly9WZW5kdWUuY29tIiwiYXVkIjoiaHR0cHM6Ly9WZW5kdWUuY29tIn0.ZYwvAym1x9GdBrf4YxSHF1xPDGDU3HiWboymwOs_v5Ml7ECS2Q4yjtk1C7mi1UUk0XxiVP-8uyb3JcWyQ3rQlQ"
//
//
//        sessionManager?.LoginSession()

        userViewModel = ViewModelProvider(this)[UserAuthViewModel::class.java]

        if(sessionManager?.isLoggedin == true){

            userViewModel.myProfile()

            userObserveViewModel()

        }

        setContentView(view)

    }


    fun userObserveViewModel() {

        userViewModel.getUser.observe(this){

            if(it.disabled){

                userViewModel.logoutApi()

                userViewModel.dataLogout.observe(this){

                    val intent = Intent(this, SplashActivity::class.java)

                    startActivity(intent)

                    finish()

                }

            }

        }

    }


    private fun initSplash() {

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, MainActivity::class.java)

            intent.putExtra("fragType", 1 )

            startActivity(intent)

            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

            finish()

        }, 3000)

    }

}