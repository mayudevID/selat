package com.ppm.selat.detail_car

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import com.ppm.selat.databinding.ActivityDetailCarBinding

class DetailCarActivity : AppCompatActivity() {

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

        //BackSystemPressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backFromPage()
            }
        })
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            backFromPage()
        }
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