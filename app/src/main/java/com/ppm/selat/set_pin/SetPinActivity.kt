package com.ppm.selat.set_pin

import android.content.Intent
import android.hardware.biometrics.BiometricManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.ppm.selat.R
import com.ppm.selat.databinding.ActivitySetPinBinding
import java.util.concurrent.Executor

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