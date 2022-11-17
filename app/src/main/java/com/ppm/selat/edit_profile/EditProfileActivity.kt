package com.ppm.selat.edit_profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.utils.*
import com.ppm.selat.databinding.ActivityEditProfileBinding
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.widget.onSnackError
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditProfileActivity : AppCompatActivity() {

    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val c1 = Calendar.getInstance()
        c1.add(Calendar.YEAR, -18)
        binding.datePicker.maxDate = c1.timeInMillis

        val c2 = Calendar.getInstance()
        c2.add(Calendar.YEAR, -100)
        binding.datePicker.minDate = c2.timeInMillis

        supportActionBar?.hide()
        binding.errorText.text = ""

        editProfileViewModel.editMode = intent.getEnumExtra<TypeDataEdit>()!!

        setUpData()
        setUpListener()
    }

    private fun setUpData() {
        when (editProfileViewModel.editMode) {
            TypeDataEdit.NAME -> {
                binding.title.text = "Ubah Nama Lengkap"
                binding.textBase.text = "Nama Lengkap"
                binding.editTextBase.hint = "Nama Lengkap"
                binding.editTextBase.inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            }
            TypeDataEdit.PDOB -> {
                binding.title.text = "Ubah Tempat Tanggal Lahir"
                binding.textBase.text = "Tempat Tanggal Lahir"

                binding.editTextBase.visibility = View.GONE
                binding.editPlaceTextBaseLayout.visibility = View.VISIBLE
                binding.editPlaceTextBase.hint = "Tempat lahir"

                binding.datePicker.visibility = View.VISIBLE
                binding.setDateBirth.visibility = View.VISIBLE
            }
            TypeDataEdit.EMAIL -> {
                binding.title.text = "Ubah Email"
                binding.textBase.text = "Email"
                binding.editTextBase.hint = "Email"
                binding.editTextBase.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            TypeDataEdit.PHONE -> {
                binding.title.text = "Ubah No Telpon"
                binding.textBase.text = "No Telpon"
                binding.editTextBase.hint = "No Telpon"
                binding.editTextBase.inputType = InputType.TYPE_CLASS_PHONE
            }
            TypeDataEdit.JOB -> {
                binding.title.text = "Ubah Pekerjaan"
                binding.textBase.text = "Pekerjaan"
                binding.editTextBase.hint = "Pekerjaan"
                binding.editTextBase.inputType = InputType.TYPE_CLASS_TEXT
            }
            else -> {}
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUpListener() {

        binding.backButtonEdit.setOnClickListener {
            finish()
        }

        binding.setDateBirth.setOnClickListener {
            val year: Int = binding.datePicker.year
            val month: Int = binding.datePicker.month
            val day: Int = binding.datePicker.dayOfMonth

            val calendar: Calendar = Calendar.getInstance()
            calendar.set(year, month, day)

            val format = SimpleDateFormat("dd MMMM yyyy")

            editProfileViewModel.dateBirth = format.format(calendar.time)
            binding.dateTemp.text = editProfileViewModel.dateBirth
        }

        binding.editTextBase.doAfterTextChanged {
            val value = it.toString().trim()
            when (editProfileViewModel.editMode) {
                TypeDataEdit.NAME -> {
                    if (value.length > 4 || value == "") {
                        binding.errorText.text = ""
                        binding.saveButton.isClickable = true
                        editProfileViewModel.textValue = value
                    } else {
                        binding.errorText.text = "Nama harus lebih dari 4 (empat) karakter"
                        binding.saveButton.isClickable = false
                    }
                }
                TypeDataEdit.EMAIL -> {
                    if (emailPattern.matcher(value).matches() || value == "") {
                        binding.errorText.text = ""
                        binding.saveButton.isClickable = true
                        editProfileViewModel.textValue = value
                    } else {
                        binding.errorText.text = "Email tidak valid"
                        binding.saveButton.isClickable = false
                    }
                }
                TypeDataEdit.PHONE -> {
                    if (value.length > 10 || value == "") {
                        binding.errorText.text = ""
                        binding.saveButton.isClickable = true
                        editProfileViewModel.textValue = value
                    } else {
                        binding.errorText.text = "No telpon tidak valid"
                        binding.saveButton.isClickable = false
                    }
                }
                TypeDataEdit.JOB -> {
                    editProfileViewModel.textValue = value
                }
                else -> {}
            }
        }

        binding.editPlaceTextBase.doAfterTextChanged {
            val value = it.toString().trim()
            if (value.isEmpty() || value == "") {
                binding.errorText.text = "Mohon isi nama tempat lahir"
                binding.saveButton.isClickable = false
            } else {
                binding.errorText.text = ""
                binding.saveButton.isClickable = true

                Log.d("EditProfileActivity", "$value, ${editProfileViewModel.dateBirth}")

                editProfileViewModel.textValue = "$value, ${editProfileViewModel.dateBirth}"
            }
        }

        binding.saveButton.setOnClickListener {
            dismissKeyboard(this@EditProfileActivity)
            if (editProfileViewModel.textValue.isEmpty() || editProfileViewModel.textValue == "") {
                when (editProfileViewModel.editMode) {
                    TypeDataEdit.NAME -> {
                        onSnackError("Mohon isi data", binding.root, applicationContext)
                    }
                    TypeDataEdit.EMAIL -> {
                        onSnackError("Mohon isi data", binding.root, applicationContext)
                    }
                    TypeDataEdit.ADDRESS -> {
                        onSnackError("Mohon isi data", binding.root, applicationContext)
                    }
                    TypeDataEdit.PHONE -> {
                        onSnackError("Mohon isi data", binding.root, applicationContext)
                    }
                    TypeDataEdit.PDOB -> {
                        onSnackError("Mohon isi data", binding.root, applicationContext)
                    }
                    else -> {
                        sendData()
                    }
                }
            } else {
                if (editProfileViewModel.editMode == TypeDataEdit.EMAIL) {
                    showPasswordConfirm()
                } else if (editProfileViewModel.editMode == TypeDataEdit.PDOB) {
                    val dateTemp = editProfileViewModel.dateBirth
                    if (dateTemp.isEmpty() || dateTemp == "") {
                        onSnackError("Mohon isi data tempat dan tanggal lahir", binding.root, applicationContext)
                    } else {
                        sendData()
                    }
                } else {
                    sendData()
                }

            }
        }
    }

    private fun sendData() {
        Log.d("EditProfileActivity", "CALL SEND DATA")
        if (isNetworkAvailable(this@EditProfileActivity)) {
            val dialog = startLoadingDialog("Simpan...", this)
            editProfileViewModel.updateProfile().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            Toast.makeText(this, "Sukses disimpan", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                            finish()
                        }
                        is Resource.Error -> {
                            onSnackError(result.message!!, binding.root, applicationContext)
                            dialog.dismiss()
                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, applicationContext)
        }
    }

    private fun checkPasswordAndProcessing(password: String) {
        fun processingUpdateEmail(loadDialog: AlertDialog) {
            editProfileViewModel.updateProfile().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            Toast.makeText(this, "Sukses disimpan", Toast.LENGTH_SHORT).show()
                            loadDialog.dismiss()
                            finish()
                        }
                        is Resource.Error -> {
                            onSnackError(result.message!!, binding.root, applicationContext)
                            loadDialog.dismiss()
                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }

        if (isNetworkAvailable(this@EditProfileActivity)) {
            val loadDialog = startLoadingDialog("Sedang memproses...", this)
            editProfileViewModel.getPassword().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            if (password == result.data) {
                                processingUpdateEmail(loadDialog)
                            } else {
                                onSnackError(result.message!!, binding.root, applicationContext)
                                loadDialog.dismiss()
                                showPasswordConfirm()
                            }
                        }
                        is Resource.Error -> {
                            onSnackError(result.message!!, binding.root, applicationContext)
                            loadDialog.dismiss()
                            showPasswordConfirm()
                        }
                        is Resource.Loading -> {}
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhbung ke internet", binding.root, applicationContext)
        }
    }

    private fun showPasswordConfirm() {
        var passwordEmpty = true
        var passwordValue : String? = null

        val dialogView = layoutInflater.inflate(R.layout.dialog_set_password, null)
        val customDialog = AlertDialog.Builder(this).setView(dialogView).create()

        customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        customDialog.window?.setLayout(950, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.setCanceledOnTouchOutside(false)

        val okButton = dialogView.findViewById<TextView>(R.id.ok_button)
        val cancel = dialogView.findViewById<TextView>(R.id.cancel_button)
        val errorMessage = dialogView.findViewById<TextView>(R.id.error_text)
        val password = dialogView.findViewById<EditText>(R.id.edit_text_pass_confirm)

        password.doAfterTextChanged {
            val stringTemp = it.toString().trim()
            passwordEmpty = stringTemp == "" || stringTemp.isEmpty()

            if (stringTemp.length < 6) {
                errorMessage.text = "Password minimal 6 (enam) karakter"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            } else {
                errorMessage.alpha = 0F
                okButton.isClickable = true
                passwordValue = AESEncryption.encrypt(password.text.toString().trim())
            }
        }

        okButton.setOnClickListener {
            if (!passwordEmpty) {
                checkPasswordAndProcessing(passwordValue!!)
                customDialog.dismiss()
            } else {
                errorMessage.text = "Mohon isi data"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            }
        }

        cancel.setOnClickListener {

        }

        customDialog.show()

//        fun initFocus() {
//            val imm =
//                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//            Handler(Looper.getMainLooper()).postDelayed({
//                firstPin.requestFocus();
//                imm.showSoftInput(firstPin, 0);
//            }, 100)
//        }

//        initFocus()

        cancel.setOnClickListener {
            customDialog.dismiss()
        }
    }
}