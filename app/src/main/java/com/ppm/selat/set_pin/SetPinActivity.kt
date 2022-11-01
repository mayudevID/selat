package com.ppm.selat.set_pin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.databinding.ActivitySetPinBinding

class SetPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPinBinding
    private var siteKey: String = "6LeAgroiAAAAANh9aZlr84gHyTeRqNsU60qavCiR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.firstPinView.setAnimationEnable(true)
        binding.firstPinView.isPasswordHidden = true

        setUpListener()
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}