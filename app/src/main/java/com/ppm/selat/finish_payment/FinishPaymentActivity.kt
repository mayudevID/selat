package com.ppm.selat.finish_payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.databinding.ActivityFinishPaymentBinding

class FinishPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishPaymentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}