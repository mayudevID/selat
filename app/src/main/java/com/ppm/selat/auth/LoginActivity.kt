package com.ppm.selat.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.transition.Fade
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import com.ppm.selat.R

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var obsecureButton: ImageButton
    private lateinit var loginButton: FrameLayout
    var isClosed: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        setUpLayout()
        setUpListener()
    }

    private fun setUpLayout() {
        emailEditText = findViewById(R.id.email_edit_text)
        passwordEditText = findViewById(R.id.pass_edit_text)
        obsecureButton = findViewById(R.id.obsecure)
        loginButton = findViewById(R.id.login_button)
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

        loginButton.setOnClickListener {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }

        findViewById<TextView>(R.id.to_register).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}