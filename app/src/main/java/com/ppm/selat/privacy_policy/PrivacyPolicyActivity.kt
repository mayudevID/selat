package com.ppm.selat.privacy_policy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.databinding.ActivityPrivacyPolicyBinding

class PrivacyPolicyActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPrivacyPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListener()
    }

    private fun setUpListener() {
        binding.backButtonPp.setOnClickListener {
            finish()
        }
    }
}