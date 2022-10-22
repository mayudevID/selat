package com.ppm.selat.edit_profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileActivity : AppCompatActivity() {

    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: ActivityEditProfileBinding
    private val customLoadingDialog = CustomLoadingDialog(this)

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(this) // optional usage
            Glide.with(this)
                .load(uriContent)
                .into(binding.circleImageView)
            editProfileViewModel.photoIsChanged.value = true
        } else {
            val exception = result.error
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        initDataAndValidForm()
        setUpListener()
    }

    private fun initDataAndValidForm() {
        binding.editedFullName.visibility = View.GONE
        binding.editedEmail.visibility = View.GONE

        editProfileViewModel.userDataStream.asLiveData().observe(this) {
            binding.userName.text = it.name
        }

        lifecycleScope.launch {
            val data = editProfileViewModel.userDataStream.first()

            editProfileViewModel.nameInit = data.name.toString()
            editProfileViewModel.nameFlow.value = data.name.toString()
            binding.editTextFullName.setText(data.name.toString())

            editProfileViewModel.emailInit = data.email.toString()
            editProfileViewModel.emailFlow.value = data.email.toString()
            binding.editTextEmail.setText(data.email.toString())

            Glide.with(this@EditProfileActivity)
                .load(data.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.circleImageView)
        }

        with(binding) {
            editTextFullName.doAfterTextChanged {
                editProfileViewModel.nameFlow.value = it.toString().trim()
                if (editProfileViewModel.checkNameIsChanged()) {
                    binding.editedFullName.visibility = View.VISIBLE
                    binding.cancelSaveFullName.visibility = View.VISIBLE
                } else {
                    binding.editedFullName.visibility = View.GONE
                    binding.cancelSaveFullName.visibility = View.GONE
                }
                Log.d("EditProfileActivity", editProfileViewModel.checkNameIsChanged().toString())
            }
            editTextEmail.doAfterTextChanged {
                editProfileViewModel.emailFlow.value = it.toString().trim()
                if (editProfileViewModel.checkEmailIsChanged()) {
                    binding.editedEmail.visibility = View.VISIBLE
                    binding.cancelSaveEmail.visibility = View.VISIBLE
                } else {
                    binding.editedEmail.visibility = View.GONE
                    binding.cancelSaveEmail.visibility = View.GONE
                }
                Log.d("EditProfileActivity", editProfileViewModel.checkEmailIsChanged().toString())
            }
        }
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.circleImageView.setOnClickListener {
            pickImageForProfilePicture()
        }

        // full name changed handler
        binding.cancelFullNameButton.setOnClickListener {
            dismissKeyboard()
            binding.editTextFullName.setText(editProfileViewModel.nameInit)
        }
        binding.saveFullNameButton.setOnClickListener {
            dismissKeyboard()
            if (isNetworkAvailable()) {
                val dialog = customLoadingDialog.startLoadingDialog("Simpan nama...")
                editProfileViewModel.saveNewName().observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                Toast.makeText(this, "Nama diperbarui", Toast.LENGTH_SHORT).show()
                                editProfileViewModel.nameInit = editProfileViewModel.nameFlow.value
                                binding.editedFullName.visibility = View.GONE
                                binding.cancelSaveFullName.visibility = View.GONE
                                dialog.dismiss()
                            }
                            is Resource.Error -> {
                                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Tidak dapat terhubung ke internet", Toast.LENGTH_SHORT).show()
            }
        }

        // email changed handler
        binding.cancelEmailButton.setOnClickListener {
            binding.editTextEmail.setText(editProfileViewModel.emailInit)
        }
        binding.saveEmailButton.setOnClickListener {

        }
    }

    private fun pickImageForProfilePicture() {
        cropImage.launch(
            options {
                setToolbarColor(Color.BLACK)
                setImageSource(includeGallery = true, includeCamera = false)
                setCropShape(CropImageView.CropShape.OVAL)
                setGuidelines(CropImageView.Guidelines.ON)
                setAspectRatio(1, 1)
                setFixAspectRatio(true)
                setOutputCompressFormat(Bitmap.CompressFormat.PNG)
            }
        )
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo()!!
            .isConnected()
    }

    private fun setLoadingDialog(textLoad: String) {
    }

    private fun dismissKeyboard() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}

//if (isConnected) {
//                val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
//                val customDialog = AlertDialog.Builder(this).setView(dialogView).create()
//                customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
//                customDialog.window?.setLayout(300, 300)
//                customDialog.setCanceledOnTouchOutside(false)
//                editProfileViewModel.saveProfile().observe(this) {
//                    result ->
//                        if (result != null) {
//                            when (result) {
//                                is Resource.Success -> {
//                                    customDialog.dismiss()
//                                    finish()
//                                }
//                                is Resource.Error -> {
//                                    customDialog.dismiss()
//                                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
//                                }
//                                is Resource.Loading -> {
//                                    customDialog.show()
//                                }
//                            }
//                        }
//                }
//            } else {
//                Toast.makeText(this, "Tidak terhubung ke internet", Toast.LENGTH_SHORT).show()
//            }