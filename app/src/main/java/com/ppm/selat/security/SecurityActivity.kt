package com.ppm.selat.security

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.databinding.ActivitySecurityBinding
import com.ppm.selat.login_history.LoginHistoryActivity

class SecurityActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecurityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListener()
    }

    private fun setUpListener() {
        binding.backButtonSecurity.setOnClickListener {
            finish()
        }

        binding.text3Button.setOnClickListener {
            val intent = Intent(this@SecurityActivity, LoginHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}