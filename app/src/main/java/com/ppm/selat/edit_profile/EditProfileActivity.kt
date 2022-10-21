package com.ppm.selat.edit_profile

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AlertDialogLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text

class EditProfileActivity : AppCompatActivity() {

    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var customDialog: AlertDialog

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
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

        initDataAndValidForm()
        setUpListener()
    }

    private fun initDataAndValidForm() {
        binding.editedFullName.visibility = View.GONE
        binding.editedEmail.visibility = View.GONE

        lifecycleScope.launch {
            val data = editProfileViewModel.userDataStream.first()

            editProfileViewModel.nameInit = data.name.toString()
            editProfileViewModel.nameFlow.value = data.name.toString()
            binding.userName.text = data.name.toString()
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
            binding.editTextFullName.setText(editProfileViewModel.nameInit)
        }
        binding.saveFullNameButton.setOnClickListener {
            editProfileViewModel.saveNewName().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {
                            setLoadingDialog("Simpan nama...")
                        }
                        is Resource.Success -> {
                            Toast.makeText(this, "Nama diperbarui", Toast.LENGTH_SHORT).show()
                            editProfileViewModel.nameInit = editProfileViewModel.nameFlow.value
                            binding.editedFullName.visibility = View.GONE
                            binding.cancelSaveFullName.visibility = View.GONE
                            customDialog.dismiss()
                        }
                        is Resource.Error -> {

                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                            customDialog.dismiss()
                        }
                    }
                }
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
                setImageSource(includeGallery = true, includeCamera = false)
                setCropShape(CropImageView.CropShape.OVAL)
                setGuidelines(CropImageView.Guidelines.ON)
                setAspectRatio(1, 1)
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

    private fun setLoadingDialog(textLoad: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
        customDialog = AlertDialog.Builder(this).setView(dialogView).show()
        customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        customDialog.window?.setLayout(400, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.window?.setGravity(Gravity.CENTER)
        customDialog.setCanceledOnTouchOutside(false)
        val textLoading = dialogView.findViewById<TextView>(R.id.text_desc_cpi)
        textLoading.text = textLoad
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