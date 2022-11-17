package com.ppm.selat.change_password

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityChangePasswordBinding
import com.ppm.selat.widget.onSnackError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.Duration.Companion.seconds

class ChangePasswordActivity : AppCompatActivity() {

    private val changePasswordViewModel: ChangePasswordViewModel by viewModel()
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var targetEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        lifecycleScope.launch {
            changePasswordViewModel.getUserNow().collectLatest {
                targetEmail = it.email.toString()
            }
        }

        System.currentTimeMillis().seconds.inWholeSeconds

        setUpListener()
    }

    private fun setUpListener() {
        binding.backButtonChangePassword.setOnClickListener {
            finish()
        }

        binding.buttonSendEmail.setOnClickListener {
            binding.buttonSendEmail.isClickable = false
            binding.buttonSendEmail.alpha = 0.4F
            sendDataEmail()
        }
    }

    private fun sendDataEmail() {
        changePasswordViewModel.resetPassword(targetEmail).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Resource.Success -> {
                        finish()
                        Toast.makeText(
                            this@ChangePasswordActivity,
                            "Email konfirmasi untuk reset password telah terkirim. Silakan cek kotak masuk atau spam lebih lanjut",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resource.Error -> {
                        Log.d("ResetPasswordActivity", result.message.toString())
                        onSnackError(result.message.toString(), binding.root, applicationContext)
                    }
                    is Resource.Loading -> {

                    }
                }
            }
        }
    }
}