package com.ppm.selat.auth.reset_password

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.utils.emailPattern
import com.ppm.selat.databinding.ActivityResetPasswordBinding
import com.ppm.selat.widget.onSnackError
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : AppCompatActivity() {

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModel()
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.emailError.text = ""

        setValidForm()
        setUpListener()
    }

    private fun setValidForm() {
        lifecycleScope.launch {
            resetPasswordViewModel.emailIsValid.collect {
                if (it) {
                    binding.emailError.text = ""
                    binding.sendButton.isClickable = true
                } else {
                    binding.emailError.text = "Email tidak valid"
                    binding.sendButton.isClickable = false
                }
            }
        }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            resetPasswordViewModel.emailInput.value = text.toString().trim()
            resetPasswordViewModel.emailIsValid.value =
                if (resetPasswordViewModel.emailInput.value == "") true else emailPattern.matcher(
                    resetPasswordViewModel.emailInput.value
                ).matches()
        }
    }

    private fun setUpListener() {
        binding.sendButton.setOnClickListener {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            if (resetPasswordViewModel.emailInput.value == "") {
                onSnackError("Mohon isi email", binding.root, applicationContext)
            } else {
                resetPasswordViewModel.resetPassword().observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Resource.Success -> {
                                binding.sendTimer.visibility = View.VISIBLE
                                binding.sendButton.visibility = View.GONE
                                var num = 60
                                val handler = Handler(Looper.getMainLooper()!!)
                                val run: Runnable = object : Runnable {
                                    override fun run() {
                                        num--
                                        binding.sendTimer.text =
                                            "Menunggu $num detik untuk mengirim email kembali"
                                        if (num > 0) {
                                            handler.postDelayed(this, 1000)
                                        } else {
                                            binding.sendButton.visibility = View.VISIBLE
                                            binding.sendTimer.visibility = View.GONE
                                        }
                                    }
                                }
                                handler.post(run)
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

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}