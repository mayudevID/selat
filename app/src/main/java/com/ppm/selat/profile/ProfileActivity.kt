package com.ppm.selat.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.ppm.selat.R
import com.ppm.selat.auth.login.LoginActivity
import com.ppm.selat.core.data.Resource
import com.ppm.selat.databinding.ActivityProfileBinding
import com.ppm.selat.detail_profile.DetailProfileActivity
import com.ppm.selat.security.SecurityActivity
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.terms_conditions.TermsConditionsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {

    private val profileViewModel: ProfileViewModel by viewModel()
    private lateinit var binding: ActivityProfileBinding

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val uriContent = result.uriContent
            showPickDialog(uriContent!!)
        }
    }

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

        binding.editPhotoButton.setOnClickListener {
            pickImageForProfilePicture()
        }

        binding.keluarButton.setOnClickListener {
            showExitDialog()
        }

        binding.dataPribadiButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, DetailProfileActivity::class.java)
            startActivity(intent)
        }

        binding.sdankButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, TermsConditionsActivity::class.java)
            startActivity(intent)
        }

        binding.keamananButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, SecurityActivity::class.java)
            startActivity(intent)
        }

        binding.kebijakanPrivasiButton.setOnClickListener {
            val intent = Intent(this@ProfileActivity, TermsConditionsActivity::class.java)
            startActivity(intent)
        }

        binding.pusatBantuanButton.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=+6288289992962"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
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

    private fun changeProfilePicture(photo: Uri) {
        if (isNetworkAvailable()) {
            val dialogLoading = startLoadingDialog("Simpan foto...", this)
            profileViewModel.photoFlow.value = photo
            profileViewModel.saveNewProfile().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            Toast.makeText(this, "Foto profil diperbarui", Toast.LENGTH_SHORT).show()
                            dialogLoading.dismiss()
                            finish()
                        }
                        is Resource.Error -> {
                            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                            dialogLoading.dismiss()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(this, "Tidak dapat terhubung ke internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPickDialog(photo: Uri)  {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_pick_photo, null)

        builder.setView(dialogView)

        dialog = builder.create()
        dialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        dialog.window?.setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)

        val cancelButton = dialogView.findViewById<TextView>(R.id.cancel_button)
        cancelButton.setOnClickListener{
            dialog.dismiss()
        }
        val okButton = dialogView.findViewById<TextView>(R.id.ok_button)
        okButton.setOnClickListener{
            dialog.dismiss()
            changeProfilePicture(photo)
        }
        val imageView = dialogView.findViewById<ImageView>(R.id.image_temp)

        Glide.with(dialogView)
            .load(photo)
            .into(imageView)

        dialog.show()
    }

    private fun showExitDialog() {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_logout, null)

        builder.setView(dialogView)

        dialog = builder.create()
        dialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        dialog.window?.setLayout(800, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)

        val cancelButton = dialogView.findViewById<TextView>(R.id.cancel_button)
        cancelButton.setOnClickListener{
            dialog.dismiss()
        }
        val okButton = dialogView.findViewById<TextView>(R.id.ok_button)
        okButton.setOnClickListener{
            dialog.dismiss()
            val dialogLoading = startLoadingDialog("Proses keluar...", this)
            profileViewModel.logoutFromFirebase().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            dialogLoading.dismiss()
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
        dialog.show()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo!!
            .isConnected
    }

    override fun onBackPressed() {
        finish()
    }
}