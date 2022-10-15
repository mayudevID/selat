package com.ppm.selat.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.ppm.selat.MyApplication
import com.ppm.selat.R
import com.ppm.selat.ViewModelFactory
import com.ppm.selat.auth.LoginActivity
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityProfileBinding
import com.ppm.selat.home.HomeViewModel
import javax.inject.Inject

class ProfileActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val profileViewModel: ProfileViewModel by viewModels {
        factory
    }

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
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
                .into(binding.circleImageView);
        }
    }

    private fun setUpListener() {
        binding.logoutButton.setOnClickListener {
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