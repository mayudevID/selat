package com.ppm.selat.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ppm.selat.auth.login.LoginActivity
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityProfileBinding
import com.ppm.selat.edit_profile.EditProfileActivity
import com.ppm.selat.set_pin.SetPinActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {
    private val profileViewModel: ProfileViewModel by viewModel()

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpProfile()
        setUpListener()
    }

    private fun setUpProfile() {
        profileViewModel.userDataStream.observe(this) {
            binding.userName.text = it.name
            Glide.with(this)
                .load(it.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.circleImageView)
        }
    }

    private fun setUpListener() {

        binding.backButton.setOnClickListener {
            finish()
        }

//        binding.pinButton.setOnClickListener {
//            val intent = Intent(this@ProfileActivity, SetPinActivity::class.java)
//            startActivity(intent)
//        }

        binding.editButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.keluarButton.setOnClickListener {
            profileViewModel.logoutFromFirebase().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        }
                        is Resource.Error -> {

                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }
}