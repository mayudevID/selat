package com.ppm.selat.edit_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.ppm.selat.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        lifecycleScope.launch {
            editProfileViewModel.userDataStream.collect {
                editProfileViewModel.nameInit = it.name.toString()
                editProfileViewModel.nameFlow.value = it.name.toString()
                editProfileViewModel.emailInit = it.name.toString()
                editProfileViewModel.emailFlow.value = it.name.toString()
            }
        }
    }
}