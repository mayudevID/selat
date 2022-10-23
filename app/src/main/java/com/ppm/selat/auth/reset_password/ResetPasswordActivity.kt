package com.ppm.selat.auth.reset_password

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R
import com.ppm.selat.auth.login.LoginViewModel
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityEditProfileBinding
import com.ppm.selat.databinding.ActivityResetPasswordBinding
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ResetPasswordActivity : AppCompatActivity() {

    private val resetPasswordViewModel: ResetPasswordViewModel by viewModel()
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.emailError.text = ""

        setValidForm()
        setUpListener()
    }

    private fun setValidForm() {
        lifecycleScope.launch {
            resetPasswordViewModel.emailIsValid.collect {
                if (it) {
                    binding.emailError.text =  ""
                    binding.sendButton.isClickable = true
                } else {
                    binding.emailError.text =  "Email tidak valid"
                    binding.sendButton.isClickable = false
                }
            }
        }

        binding.emailEditText.doOnTextChanged { text, _, _, _ ->
            resetPasswordViewModel.emailInput.value = text.toString().trim()
        }
    }

    private fun setUpListener() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

        if (resetPasswordViewModel.emailInput.value == "") {
            onSnackError("Mohon isi email")
        } else {
            resetPasswordViewModel.resetPassword().observe(this) {
                result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {

                        }
                        is Resource.Error -> {

                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }
    }

    private fun onSnackError(errorMessage: String){
        val snackbar = Snackbar.make(binding.root, convertCode(errorMessage),
            Snackbar.LENGTH_LONG).setAction("Action", null)
        val snackbarView = snackbar.view

        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        val typeface = ResourcesCompat.getFont(applicationContext, R.font.montserrat_medium)
        textView.typeface = typeface
        textView.textSize = 12f
        snackbar.show()
    }

    private fun convertCode(errorCode: String): String {
        return when (errorCode) {
            "ERROR_WRONG_PASSWORD", "ERROR_USER_NOT_FOUND" -> {
                "Email atau password salah"
            }
            "ERROR_INVALID_EMAIL" -> {
                "Email tidak valid"
            }
            "ERROR_EMAIL_ALREADY_IN_USE" -> {
                "Email sudah terdaftar"
            }
            else -> {
                errorCode
            }
        }
    }
}