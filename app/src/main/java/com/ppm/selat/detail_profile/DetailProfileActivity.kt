package com.ppm.selat.detail_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.core.utils.TypeDataEdit
import com.ppm.selat.core.utils.putExtra
import com.ppm.selat.databinding.ActivityDetailCarBinding
import com.ppm.selat.databinding.ActivityDetailProfileBinding
import com.ppm.selat.edit_profile.EditProfileActivity

class DetailProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListener()
    }

    private fun setUpListener() {
        binding.namaLengkapEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.NAME)
            startActivity(intent)
        }

        binding.ttlEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.PDOB)
            startActivity(intent)
        }

        binding.emailEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.EMAIL)
            startActivity(intent)
        }

        binding.nomorTelponEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.PHONE)
            startActivity(intent)
        }

        binding.pekerjaanEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.JOB)
            startActivity(intent)
        }

        binding.nomorTelponEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.ADDRESS)
            startActivity(intent)
        }
    }
}