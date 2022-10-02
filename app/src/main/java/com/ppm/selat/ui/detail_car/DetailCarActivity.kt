package com.ppm.selat.ui.detail_car

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.R
import com.ppm.selat.databinding.ActivityDetailCarBinding

class DetailCarActivity : AppCompatActivity() {

    companion object {
        const val CAR_DATA = "CAR_DATA"
    }

    private lateinit var binding: ActivityDetailCarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListener()
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}