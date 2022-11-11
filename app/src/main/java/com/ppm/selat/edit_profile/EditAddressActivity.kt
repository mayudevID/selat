package com.ppm.selat.edit_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.databinding.ActivityEditAddressBinding

class EditAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}