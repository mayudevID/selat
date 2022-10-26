package com.ppm.selat.auth.register

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.utils.emailPattern
import com.ppm.selat.databinding.ActivityRegisterBinding
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModel()

    private lateinit var binding: ActivityRegisterBinding
    var isClosed: Boolean = true
    var isClosedC: Boolean = true
    private var nameErrorMessage: String? = null
    private var emailErrorMessage: String? = null
    private var passwordErrorMessage: String? = null
    private var cPasswordErrorMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setValidForm()
        setUpListener()
    }

    private fun setValidForm() {
        val formIsValid = combine(
            registerViewModel.nameFlow,
            registerViewModel.emailFlow,
            registerViewModel.passwordFlow,
            registerViewModel.cPasswordFlow
        ) { name, email, password, cPassword ->
            binding.nameError.text = ""
            binding.emailError.text = ""
            binding.passwordError.text = ""
            binding.confirmPasswordError.text = ""
            val nameIsValid = if (name == "") true else name.length > 4
            val emailIsValid = if (email == "") true else emailPattern.matcher(email).matches()
            val passwordIsValid = if (password == "") true else password.length in 8..100
            val cPasswordIsValid = if (cPassword == "") true else password == cPassword
            nameErrorMessage = when {
                nameIsValid.not() -> "Nama harus lebih dari 4 (empat) karakter"
                else -> null
            }
            emailErrorMessage = when {
                emailIsValid.not() -> "Email tidak valid"
                else -> null
            }
            passwordErrorMessage = when {
                passwordIsValid.not() -> "Password minimal 8 (delapan) karakter"
                else -> null
            }
            cPasswordErrorMessage = when {
                cPasswordIsValid.not() -> "Password tidak cocok"
                else -> null
            }
            nameErrorMessage?.let {
                binding.nameError.text = it
            }
            emailErrorMessage?.let {
                binding.emailError.text = it
            }
            passwordErrorMessage?.let {
                binding.passwordError.text = it
            }
            cPasswordErrorMessage?.let {
                binding.confirmPasswordError.text = it
            }
            nameIsValid and emailIsValid and passwordIsValid and cPasswordIsValid
        }

        with(binding) {
            nameEditText.doOnTextChanged { text, _, _, _ ->
                registerViewModel.nameFlow.value = text.toString().trim()
            }
            emailEditText.doOnTextChanged { text, _, _, _ ->
                registerViewModel.emailFlow.value = text.toString().trim()
            }
            passEditText.doOnTextChanged { text, _, _, _ ->
                registerViewModel.passwordFlow.value = text.toString().trim()
            }
            confirmPassEditText.doOnTextChanged { text, _, _, _ ->
                registerViewModel.cPasswordFlow.value = text.toString().trim()
            }
        }

        lifecycleScope.launch {
            formIsValid.collect {
                binding.registerButton.apply {
                    isClickable = it
                }
            }
        }
    }

    private fun setUpListener() {
        binding.passObsecure.setOnClickListener {
            isClosed = !isClosed

            if (isClosed) {
                binding.passObsecure.setImageResource(R.drawable.ic_akar_icons_eye)
                binding.passEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                binding.passObsecure.setImageResource(R.drawable.ic_akar_icons_eye_slashed)
                binding.passEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }

        binding.confirmPassObsecure.setOnClickListener{
            isClosedC = !isClosedC

            if (isClosedC) {
                binding.confirmPassObsecure.setImageResource(R.drawable.ic_akar_icons_eye)
                binding.confirmPassEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                binding.confirmPassObsecure.setImageResource(R.drawable.ic_akar_icons_eye_slashed)
                binding.confirmPassEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()

            }
        }

       binding.registerButton.setOnClickListener {
           val imm =
               getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
           imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

           val nameX = registerViewModel.nameFlow.value
           val emailX = registerViewModel.emailFlow.value
           val passwordX = registerViewModel.passwordFlow.value
           val cPasswordX = registerViewModel.cPasswordFlow.value

           if (nameX == ""){
               onSnackError("Mohon isi nama")
           } else if (emailX == "" ) {
               onSnackError("Mohon isi email")
           } else if (passwordX == "") {
               onSnackError("Mohon isi password")
           } else if (cPasswordX == "") {
               onSnackError("Mohon konfirmasi password")
           } else {
               registerViewModel.registerAccount().observe(this) { result ->
                   if (result != null) {
                       when (result) {
                           is Resource.Loading<*> -> {
                               Log.d("RegisterActivity", "Loading")
                               binding.registerButton.visibility = View.GONE
                               binding.loadingLogo.visibility = View.VISIBLE
                           }
                           is Resource.Success<*> -> {
                               Log.d("RegisterActivity", "Success")
                               registerViewModel.logoutForLogin().observe(this) {
                                   resultD ->
                                    if (resultD != null) {
                                        when (resultD) {
                                            is Resource.Loading -> {}
                                            is Resource.Success -> {
                                                showCustomAlert()
                                            }
                                            is Resource.Error -> {
                                                onSnackError("Kesalahan: Harap hapus cache data atau reinstall kembali aplikasi")
                                            }
                                        }
                                    }
                               }
                           }
                           is Resource.Error<*> -> {
                               binding.registerButton.visibility = View.VISIBLE
                               binding.loadingLogo.visibility = View.GONE
                               Log.d("RegisterActivity", result.message.toString())
                               onSnackError(result.message.toString())
                           }
                       }
                   }
               }
           }
        }

        binding.backToLogin.setOnClickListener {
            finish()
        }
    }

    private fun showCustomAlert() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_finish_register, null)
        val customDialog = AlertDialog.Builder(this).setView(dialogView).create()
        customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        val btDismiss = dialogView.findViewById<TextView>(R.id.ok_button)
        customDialog.window?.setLayout(850, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.setCanceledOnTouchOutside(false)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
            finish()
        }
        customDialog.show()
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