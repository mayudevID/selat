package com.ppm.selat.detail_car

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ppm.selat.R
import com.ppm.selat.databinding.ActivityDetailCarBinding
import com.ppm.selat.ui.OnboardActivity

class DetailCarActivity : AppCompatActivity() {

    companion object {
        const val CAR_DATA = "CAR_DATA"
    }

    private lateinit var binding: ActivityDetailCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListener()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.motionTop.transitionToEnd()
        }, 0)
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            backFromPage()
        }
    }

    override fun onBackPressed() {
        backFromPage()
    }

    private fun backFromPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.motionTop.transitionToStart()
        }, 0)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 100)
    }
}