package com.ppm.selat.edit_profile

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.ppm.selat.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: ActivityEditProfileBinding

    val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this) // optional usage
            Glide.with(this)
                .load(uriContent)
                .into(binding.circleImageView)
            editProfileViewModel.photoIsChanged.value = true
        } else {
            // an error occurred
            val exception = result.error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        lifecycleScope.launch {
            val data = editProfileViewModel.userDataStream.first()
            editProfileViewModel.nameInit = data.name.toString()
            editProfileViewModel.nameFlow.value = data.name.toString()
            editProfileViewModel.emailInit = data.name.toString()
            editProfileViewModel.emailFlow.value = data.name.toString()

            binding.userName.text = data.name.toString()
            Glide.with(this@EditProfileActivity)
                .load(data.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.circleImageView)
        }

        with(binding) {
            editTextFullName.doAfterTextChanged {
               editProfileViewModel.nameFlow.value = it.toString().trim()
                Log.d("EditProfileActivity", editProfileViewModel.checkNameIsChanged().toString())
            }
            editTextEmail.doAfterTextChanged {
                editProfileViewModel.nameFlow.value = it.toString().trim()
                Log.d("EditProfileActivity", editProfileViewModel.checkEmailIsChanged().toString())
            }
        }

        setUpListener()
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.cancelButton.setOnClickListener {
            finish()
        }
        binding.circleImageView.setOnClickListener {
            pickImageForProfilePicture()
        }
    }

    private fun pickImageForProfilePicture() {
        cropImage.launch(
            options {
                setImageSource(includeGallery = true, includeCamera = false)
                setCropShape(CropImageView.CropShape.OVAL)
                setGuidelines(CropImageView.Guidelines.ON)
                setAspectRatio(1,1)
                setFixAspectRatio(true)
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
        )
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager: ConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo()!!
            .isConnected()
    }
}