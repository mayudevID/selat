package com.ppm.selat.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ppm.selat.R
import com.ppm.selat.home.HomeActivity
import com.ppm.selat.OnboardActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
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