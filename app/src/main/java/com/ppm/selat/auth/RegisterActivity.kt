package com.ppm.selat.auth

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.ppm.selat.R

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var cPasswordEditText: EditText
    private lateinit var obsecureButton: ImageButton
    private lateinit var obsecureCButton: ImageButton
    private lateinit var registerButton: FrameLayout
    var isClosed: Boolean = true
    var isClosedC: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        supportActionBar?.hide()

        setUpLayout()
        setUpListener()
    }

    private fun setUpLayout() {
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.pass_edit_text)
        cPasswordEditText = findViewById(R.id.confirm_pass_edit_text)
        obsecureButton = findViewById(R.id.obsecure)
        obsecureCButton = findViewById(R.id.confirm_pass_obsecure)
        registerButton = findViewById(R.id.register_button)
    }

    private fun setUpListener() {
        obsecureButton.setOnClickListener {
            isClosed = !isClosed

            if (isClosed) {
                obsecureButton.setImageResource(R.drawable.ic_akar_icons_eye)
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                obsecureButton.setImageResource(R.drawable.ic_akar_icons_eye_slashed)
                passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            }
        }

        obsecureCButton.setOnClickListener{
            isClosedC = !isClosedC

            if (isClosedC) {
                obsecureCButton.setImageResource(R.drawable.ic_akar_icons_eye)
                cPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            } else {
                obsecureCButton.setImageResource(R.drawable.ic_akar_icons_eye_slashed)
                cPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()

            }
        }

        registerButton.setOnClickListener {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }

        findViewById<TextView>(R.id.back_to_login).setOnClickListener {
            finish()
        }
    }
}