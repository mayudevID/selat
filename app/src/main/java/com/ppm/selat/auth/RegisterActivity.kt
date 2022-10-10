package com.ppm.selat.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.ppm.selat.R
import com.ppm.selat.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    var isClosed: Boolean = true
    var isClosedC: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListener()
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
        }

        findViewById<TextView>(R.id.back_to_login).setOnClickListener {
            finish()
        }
    }
}