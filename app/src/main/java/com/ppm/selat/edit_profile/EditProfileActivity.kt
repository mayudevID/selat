package com.ppm.selat.edit_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.R
import com.ppm.selat.databinding.ActivityDetailCarBinding
import com.ppm.selat.databinding.ActivityEditProfileBinding

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}