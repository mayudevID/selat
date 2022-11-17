package com.ppm.selat.set_pin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.chaos.view.PinView
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.utils.AESEncryption
import com.ppm.selat.core.utils.dismissKeyboard
import com.ppm.selat.databinding.ActivitySetPinBinding
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.widget.onSnackError
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder

class SetPinActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetPinBinding
    private val setPinViewModel: SetPinViewModel by viewModel()
    //private var siteKey: String = "6LeAgroiAAAAANh9aZlr84gHyTeRqNsU60qavCiR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetPinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.errorMessageOldPin.alpha = 0F
        binding.firstPinView.setAnimationEnable(true)
        binding.firstPinView.isPasswordHidden = true

        setUpListener()
    }

    private fun setUpListener() {
        binding.firstPinView.doOnTextChanged { text, start, before, count ->
            val stringTemp = text.toString().trim()
            setPinViewModel.pinEmpty.value = stringTemp == "" || stringTemp.isEmpty()

            if (text.toString().trim().length < 6) {
                binding.errorMessageOldPin.text = "PIN Tidak Lengkap"
                binding.errorMessageOldPin.alpha = 1F
                binding.sendPin.isClickable = false
            } else {
                binding.errorMessageOldPin.alpha = 0F
                binding.sendPin.isClickable = true
                setPinViewModel.PIN_TEMP.value = AESEncryption.encrypt(text.toString().trim())!!
            }
        }

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.lupaPin.setOnClickListener {
            val message = "Halo, saya ingin mengajukan penggantian PIN secara manual"
            val url = "https://api.whatsapp.com/send?phone=+6288289992962&text=${URLEncoder.encode(message, "UTF-8")}"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }

        binding.sendPin.setOnClickListener {
            dismissKeyboard(this@SetPinActivity)
            if (!setPinViewModel.pinEmpty.value) {
                val dialogLoading = startLoadingDialog("Memproses...", this@SetPinActivity)
                setPinViewModel.checkPIN().observe(this) {
                        result ->
                    if (result != null) {
                        when (result) {
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                if (setPinViewModel.PIN_TEMP.value == result.data) {
                                    dialogLoading.dismiss()
                                    showCreatePin()
                                } else {
                                    dialogLoading.dismiss()
                                    onSnackError("PIN salah, ulangi kembali", binding.root, applicationContext)
                                }
                            }
                            is Resource.Error-> {
                                dialogLoading.dismiss()
                                onSnackError(result.message.toString(), binding.root, applicationContext)
                            }
                        }
                    }
                }
            } else {
                onSnackError("Mohon isi PIN baru terlebih dahulu", binding.root, applicationContext)
            }
        }
    }

    private fun processingNewPIN() {
        val dialogLoading = startLoadingDialog("Memproses...", this@SetPinActivity)
        setPinViewModel.sendPIN().observe(this) {
            result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        dialogLoading.dismiss()
                        finish()
                        Toast.makeText(this@SetPinActivity, "Ganti PIN sukses", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Error -> {
                        dialogLoading.dismiss()
                        onSnackError(result.message.toString(), binding.root, applicationContext)
                    }
                }
            }
        }
    }

    private fun showCreatePin() {
        var step = 0
        var PIN_FIRST: String? = null
        var PIN_SECOND: String? = null
        var pinEmpty = true

        val dialogView = layoutInflater.inflate(R.layout.dialog_set_pin, null)
        val customDialog = AlertDialog.Builder(this).setView(dialogView).create()

        customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        customDialog.window?.setLayout(950, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.setCanceledOnTouchOutside(false)

        val firstPin = dialogView.findViewById<PinView>(R.id.firstPinView)
        val title = dialogView.findViewById<TextView>(R.id.title_dialog)
        val subTitle = dialogView.findViewById<TextView>(R.id.subtitle_dialog)
        val errorMessage = dialogView.findViewById<TextView>(R.id.error_text)
        val okButton = dialogView.findViewById<TextView>(R.id.ok_button)
        val cancelButton = dialogView.findViewById<TextView>(R.id.cancel_button)

        fun initFocus() {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Handler(Looper.getMainLooper()).postDelayed({
                firstPin.requestFocus();
                imm.showSoftInput(firstPin, 0);
            }, 100)
        }

        okButton.isClickable = false
        firstPin.isPasswordHidden = true
        firstPin.setAnimationEnable(true)
        initFocus()

        firstPin.doOnTextChanged { text, start, before, count ->
            val stringTemp = text.toString().trim()
            pinEmpty = stringTemp == "" || stringTemp.isEmpty()

            if (text.toString().trim().length < 6) {
                errorMessage.text = "PIN Tidak Lengkap"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            } else {
                errorMessage.alpha = 0F
                okButton.isClickable = true
                if (step == 0) {
                    PIN_FIRST = AESEncryption.encrypt(text.toString().trim())
                    Log.d("ReqisterActivity", PIN_FIRST!!)
                } else {
                    PIN_SECOND = AESEncryption.encrypt(text.toString().trim())
                    Log.d("ReqisterActivity", PIN_SECOND!!)
                }
            }
        }

        okButton.setOnClickListener {
            if (!pinEmpty) {
                if (step == 0) {
                    step = 1
                    pinEmpty = true
                    firstPin.text?.clear()
                    title.text = "Konfirmasi PIN"
                    subTitle.text = "Masukkan PIN yang sebelumnya diinput"
                } else {
                    if (PIN_FIRST != PIN_SECOND) {
                        errorMessage.text = "PIN Tidak Cocok"
                        errorMessage.alpha = 1F
                        okButton.isClickable = false
                    } else {
                        setPinViewModel.PIN_NEW.value = PIN_FIRST!!
                        customDialog.dismiss()
                        dismissKeyboard(this@SetPinActivity)
                        processingNewPIN()
                    }
                }
            } else {
                errorMessage.text = "Harap isi PIN"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            }
        }

        cancelButton.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }
}