package com.ppm.selat.auth.login


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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
import com.ppm.selat.auth.register.RegisterActivity
import com.ppm.selat.auth.reset_password.ResetPasswordActivity
import com.ppm.selat.auth.reset_password.ResetPasswordViewModel
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.utils.emailPattern
import com.ppm.selat.databinding.ActivityLoginBinding
import com.ppm.selat.home.HomeActivity
import com.ppm.selat.widget.onSnackError
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModel()

    private lateinit var binding: ActivityLoginBinding
    private var isClosed: Boolean = true
    private var emailErrorMessage: String? = null
    private var passwordErrorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setValidForm()
        setUpListener()
    }

    private fun setValidForm() {
        val formIsValid = combine(
            loginViewModel.emailFlow,
            loginViewModel.passwordFlow,
        ) { email, password ->
            binding.emailError.text = ""
            binding.passwordError.text = ""
            val emailIsValid = if (email == "") true else emailPattern.matcher(email).matches()
            val passwordIsValid = if (password == "") true else password.length in 6..100
            emailErrorMessage = when {
                emailIsValid.not() -> "Email tidak valid"
                else -> null
            }
            passwordErrorMessage = when {
                passwordIsValid.not() -> "Password minimal 6 (enam) karakter"
                else -> null
            }
            emailErrorMessage?.let {
                binding.emailError.text = it
            }
            passwordErrorMessage?.let {
                binding.passwordError.text = it
            }
            emailIsValid and passwordIsValid
        }

        with(binding) {
            emailEditText.doOnTextChanged { text, _, _, _ ->
                loginViewModel.emailFlow.value = text.toString().trim()
            }
            passEditText.doOnTextChanged { text, _, _, _ ->
                loginViewModel.passwordFlow.value = text.toString().trim()
            }
        }

        lifecycleScope.launch {
            formIsValid.collect {
                binding.loginButton.apply {
                    isClickable = it
                }
            }
        }
    }

    private fun setUpListener() {
        binding.obsecurePassword.setOnClickListener {
            isClosed = !isClosed

            if (isClosed) {
                binding.obsecurePassword.setImageResource(R.drawable.ic_akar_icons_eye)
                binding.passEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                binding.obsecurePassword.setImageResource(R.drawable.ic_akar_icons_eye_slashed)
                binding.passEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }

        binding.loginButton.setOnClickListener {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            val email = loginViewModel.emailFlow.value
            val password = loginViewModel.passwordFlow.value

            if (email == "" ) {
                onSnackError("Mohon isi email", binding.root, applicationContext)
            } else if (password == "") {
                onSnackError("Mohon isi password", binding.root, applicationContext)
            } else {
                loginViewModel.loginAccount().observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Resource.Loading -> {
                                Log.d("LoginActivity", "Loading")
                                binding.loginButton.visibility = View.GONE
                                binding.loadingLogo.visibility = View.VISIBLE
                            }
                            is Resource.Success -> {
                                Log.d("LoginActivity", "Success")
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                            }
                            is Resource.Error -> {
                                binding.loginButton.visibility = View.VISIBLE
                                binding.loadingLogo.visibility = View.GONE
                                Log.d("LoginActivity", result.message.toString())
                                onSnackError(result.message.toString(), binding.root, applicationContext)
                            }
                        }
                    }
                }
            }

            binding.twitterLogin.setOnClickListener {
                onSnackError("Fitur belum tersedia", binding.root, this@LoginActivity)
            }

            binding.googleLogin.setOnClickListener {
                onSnackError("Fitur belum tersedia", binding.root, this@LoginActivity)
            }

            binding.facebookLogin.setOnClickListener {
                onSnackError("Fitur belum tersedia", binding.root, this@LoginActivity)
            }
        }

        binding.toRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.forgotPasswordText.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}