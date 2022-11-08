package com.ppm.selat.terms_conditions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.databinding.ActivityTermsConditionsBinding

class TermsConditionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsConditionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsConditionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
    }
}