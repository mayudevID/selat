package com.ppm.selat.detail_profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.ppm.selat.core.utils.TypeDataEdit
import com.ppm.selat.core.utils.putExtra
import com.ppm.selat.databinding.ActivityDetailProfileBinding
import com.ppm.selat.edit_profile.EditProfileActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailProfileActivity : AppCompatActivity() {

    private val detailProfileViewModel: DetailProfileViewModel by viewModel()
    private lateinit var binding: ActivityDetailProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpData()
        setUpListener()
    }

    private fun setUpData() {
        lifecycleScope.launch {
            detailProfileViewModel.getDataStream().collect {
                    data ->
                binding.namaLengkapData.text = data.name
                binding.ttlData.text = data.placeDateOfBirth
                binding.emailData.text = data.email
                binding.nomorTelponData.text = data.phone
                binding.pekerjaanData.text = data.job
                binding.alamatData.text = data.address
            }
        }
    }

    private fun setUpListener() {
        binding.backButtonTrans.setOnClickListener {
            finish()
        }

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

        binding.alamatEdit.setOnClickListener {
            val intent = Intent(this@DetailProfileActivity, EditProfileActivity::class.java)
            intent.putExtra(TypeDataEdit.ADDRESS)
            startActivity(intent)
        }
    }
}