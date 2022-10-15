package com.ppm.selat.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.ppm.selat.MyApplication
import com.ppm.selat.R
import com.ppm.selat.ViewModelFactory
import com.ppm.selat.auth.LoginViewModel
import com.ppm.selat.home.HomeActivity
import com.ppm.selat.ui.OnboardActivity
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val splashViewModel: SplashViewModel by viewModels {
        factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        Handler(Looper.getMainLooper()).postDelayed({
            splashViewModel.isUserSigned().observe(this) {
                if (it == true) {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, OnboardActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }, 2500)
    }
}