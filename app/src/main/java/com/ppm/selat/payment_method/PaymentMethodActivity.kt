package com.ppm.selat.payment_method

import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.R
import com.ppm.selat.auth.login.LoginActivity
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.ui.payment_method.ListPaymentMethodAdapter
import com.ppm.selat.core.utils.AESEncryption
import com.ppm.selat.core.utils.dismissKeyboard
import com.ppm.selat.core.utils.isNetworkAvailable
import com.ppm.selat.core.utils.setLogo
import com.ppm.selat.databinding.ActivityPaymentMethodBinding
import com.ppm.selat.payment.PaymentViewModel
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.widget.onSnackError
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentMethodActivity : AppCompatActivity() {

    private val paymentMethodViewModel: PaymentMethodViewModel by viewModel()
    private lateinit var listPaymentMethodAdapter: ListPaymentMethodAdapter
    private lateinit var binding: ActivityPaymentMethodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentMethodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.rvAkunTerhubung.layoutManager = LinearLayoutManager(this)
        binding.rvAkunTerhubung.setHasFixedSize(false)
        listPaymentMethodAdapter = ListPaymentMethodAdapter(arrayListOf())
        binding.rvAkunTerhubung.adapter = listPaymentMethodAdapter

        setAdapterPaymentMethod()
        setUpListener()
    }

    private fun setUpListener() {
        listPaymentMethodAdapter.setOnItemClickCallback(object :
            ListPaymentMethodAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataTypePay) {
                showDeleteDialog(data)
            }

            override fun onItemDeleted(data: Int) {
                TODO("Not yet implemented")
            }

        })

        binding.retryButton.setOnClickListener {
            setAdapterPaymentMethod()
        }

        binding.backButtonMp.setOnClickListener {
            finish()
        }

        binding.kkButton.setOnClickListener {
            showAddPaymentMethod("CARD", "MASTERCARD")
        }

        binding.gopayButton.setOnClickListener {
            showAddPaymentMethod("EWALLET", "GOPAY")
        }

        binding.ovoButton.setOnClickListener {
            showAddPaymentMethod("EWALLET", "OVO")
        }

        binding.danaButton.setOnClickListener {
            showAddPaymentMethod("EWALLET", "DANA")
        }

        binding.paypalButton.setOnClickListener {
            showAddPaymentMethod("EWALLET", "PAYPAL")
        }
    }

    private fun savePaymentMethod(dataTypePay: DataTypePay) {
        if (isNetworkAvailable(this@PaymentMethodActivity)) {
            val dialogLoading = startLoadingDialog("Menghubungkan...", this@PaymentMethodActivity)
            paymentMethodViewModel.savePaymentMethod(dataTypePay).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            dialogLoading.dismiss()
                            setAdapterPaymentMethod()
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Error -> {
                            dialogLoading.dismiss()
                            onSnackError(
                                "Gagal menambahkan metode pembayaran",
                                binding.root,
                                this@PaymentMethodActivity
                            )
                        }
                    }
                }
            }
        } else {
            onSnackError(
                "Gagal menambahkan metode pembayaran",
                binding.root,
                this@PaymentMethodActivity
            )
        }
    }

    private fun deletePaymentMethod(dataTypePay: DataTypePay) {
        if (isNetworkAvailable(this@PaymentMethodActivity)) {
            val dialogLoading = startLoadingDialog("Hapus...", this@PaymentMethodActivity)
            paymentMethodViewModel.deletePaymentMethod(dataTypePay).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            dialogLoading.dismiss()
                            setAdapterPaymentMethod()
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Error -> {
                            dialogLoading.dismiss()
                            onSnackError(
                                "Gagal unbind metode pembayaran",
                                binding.root,
                                this@PaymentMethodActivity
                            )
                        }
                    }
                }
            }
        } else {
            onSnackError("Gagal unbind metode pembayaran", binding.root, this@PaymentMethodActivity)
        }
    }

    private fun setAdapterPaymentMethod() {
        binding.rvAkunTerhubung.visibility = View.GONE
        binding.errorMessage.visibility = View.GONE
        binding.emptyList.visibility = View.GONE
        binding.loadPaymentMethodList.visibility = View.VISIBLE

        if (isNetworkAvailable(this@PaymentMethodActivity)) {
            val dialogLoading = startLoadingDialog("Loading list...", this@PaymentMethodActivity)
            paymentMethodViewModel.getListPaymentMethod().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            if (result.data!!.isNotEmpty()) {
                                dialogLoading.dismiss()
                                val data = ArrayList(result.data!!)
                                listPaymentMethodAdapter.refreshData(data)
                                binding.loadPaymentMethodList.visibility = View.GONE
                                binding.rvAkunTerhubung.visibility = View.VISIBLE
                            } else {
                                dialogLoading.dismiss()
                                binding.loadPaymentMethodList.visibility = View.GONE
                                binding.emptyList.visibility = View.VISIBLE
                            }
                        }
                        is Resource.Loading -> {

                        }
                        is Resource.Error -> {
                            binding.errorMessage.visibility = View.VISIBLE
                        }
                    }
                }
            }
        } else {
            binding.rvAkunTerhubung.visibility = View.GONE
            binding.emptyList.visibility = View.GONE
            binding.loadPaymentMethodList.visibility = View.GONE
            binding.errorMessage.visibility = View.VISIBLE
        }
    }

    private fun showAddPaymentMethod(type: String, name: String) {
        var noEmpty = true
        var noValue: String? = null

        val dialogView = layoutInflater.inflate(R.layout.dialog_add_payment_method, null)
        val customDialog = AlertDialog.Builder(this).setView(dialogView).create()

        customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        customDialog.window?.setLayout(950, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.setCanceledOnTouchOutside(false)

        val okButton = dialogView.findViewById<TextView>(R.id.ok_button)
        val cancel = dialogView.findViewById<TextView>(R.id.cancel_button)
        val errorMessage = dialogView.findViewById<TextView>(R.id.error_text)
        val noValueEditText = dialogView.findViewById<EditText>(R.id.edit_text_no)
        val logo = dialogView.findViewById<ImageView>(R.id.target_logo_pm)

        logo.setImageResource(setLogo(name))

        noValueEditText.doAfterTextChanged {
            val stringTemp = it.toString().trim()
            noEmpty = stringTemp == "" || stringTemp.isEmpty()

            if (type == "CARD" && stringTemp.length < 16) {
                errorMessage.text = "No VCC minimal 16 (enam belas) karakter"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            } else if (type == "EWALLET" && stringTemp.length < 10) {
                errorMessage.text = "No telepon minimal 10 (sepuluh) karakter"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            } else {
                errorMessage.alpha = 0F
                okButton.isClickable = true
                noValue = if (type == "CARD") {
                    noValueEditText.text.toString().replace("....".toRegex(), "$0 ")
                } else {
                    noValueEditText.text.toString().trim()
                }
            }
        }

        okButton.setOnClickListener {
            if (!noEmpty) {
                customDialog.dismiss()
                savePaymentMethod(
                    DataTypePay(
                        id = "0",
                        number = noValue!!,
                        type = type,
                        value = 1500000,
                        name = name
                    )
                )
            } else {
                errorMessage.text = "Mohon isi data"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            }
        }

        cancel.setOnClickListener {
            customDialog.dismiss()
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

    private fun showDeleteDialog(dataTypePay: DataTypePay) {
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
        cancelButton.setOnClickListener {
            dialog.dismiss()
        }
        val okButton = dialogView.findViewById<TextView>(R.id.ok_button)
        okButton.setOnClickListener {
            dialog.dismiss()
            deletePaymentMethod(dataTypePay)
        }
        dialog.show()
    }
}