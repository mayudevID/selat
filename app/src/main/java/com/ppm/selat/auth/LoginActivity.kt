package com.ppm.selat.auth


import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.MyApplication
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.presentation.ViewModelAuthFactory
import com.ppm.selat.core.utils.emailPattern
import com.ppm.selat.databinding.ActivityLoginBinding
import com.ppm.selat.home.HomeActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelAuthFactory

    private val loginViewModel: LoginViewModel by viewModels {
        factory
    }

    private lateinit var binding: ActivityLoginBinding
    private var isClosed: Boolean = true
    private var emailErrorMessage: String? = null
    private var passwordErrorMessage: String? = null
    private val emailFlow = MutableStateFlow("")
    private val passwordFlow = MutableStateFlow("")

    private val formIsValid = combine(
        emailFlow,
        passwordFlow,
    ) { email, password ->
        binding.emailError.text = ""
        binding.passwordError.text = ""
        val emailIsValid = if (email == "") true else emailPattern.matcher(email).matches()
        val passwordIsValid = if (password == "") true else password.length in 8..100
        emailErrorMessage = when {
            emailIsValid.not() -> "Email tidak valid"
            else -> null
        }
        passwordErrorMessage = when {
            passwordIsValid.not() -> "Password minimal 8 (delapan) karakter"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        
        with(binding) {
            emailEditText.doOnTextChanged { text, _, _, _ ->
                emailFlow.value = text.toString().trim()
            }
            passEditText.doOnTextChanged { text, _, _, _ ->
                passwordFlow.value = text.toString().trim()
            }
        }

        lifecycleScope.launch {
            formIsValid.collect {
                binding.loginButton.apply {
                    isClickable = it
                }
            }
        }

        setUpListener()
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

            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passEditText.text.toString().trim()

            if (email == "" ) {
                onSnackError("Mohon isi email")
            } else if (password == "") {
                onSnackError("Mohon isi password")
            } else {
                loginViewModel.loginAccount(email, password).observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Resource.Loading<*> -> {
                                Log.d("LoginActivity", "Loading")
                                binding.loginButton.visibility = View.GONE
                                binding.loadingLogo.visibility = View.VISIBLE
                            }
                            is Resource.Success<*> -> {
                                Log.d("LoginActivity", "Success")
                                startActivity(Intent(this,HomeActivity::class.java))
                                finish()
                                loginViewModel.loginAccount(email, password).removeObservers(this)
                            }
                            is Resource.Error<*> -> {
                                binding.loginButton.visibility = View.VISIBLE
                                binding.loadingLogo.visibility = View.GONE
                                Log.d("LoginActivity", result.message.toString())
                                onSnackError(result.message.toString())
                                loginViewModel.loginAccount(email, password).removeObservers(this)
                            }
                        }
                    }
                }
            }

        }

        binding.toRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
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
            else -> {
                errorCode
            }
        }
    }
}