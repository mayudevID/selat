package com.ppm.selat.payment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.chaos.view.PinView
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.ui.payment.ListCardAdapter
import com.ppm.selat.core.ui.payment.ListEWalletAdapter
import com.ppm.selat.core.utils.AESEncryption
import com.ppm.selat.core.utils.isNetworkAvailable
import com.ppm.selat.core.utils.setLogo
import com.ppm.selat.databinding.ActivityPaymentBinding
import com.ppm.selat.finish_payment.FinishPaymentActivity
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.widget.convertCode
import com.ppm.selat.widget.onSnackError
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import org.koin.androidx.viewmodel.ext.android.viewModel


class PaymentActivity : AppCompatActivity() {

    private val paymentViewModel: PaymentViewModel by viewModel()
    private lateinit var binding: ActivityPaymentBinding
    private var pinAttempt = 0
    private lateinit var carData: Car
    private lateinit var listDataTypePay: ArrayList<DataTypePay>
    private lateinit var kursIndonesia1: DecimalFormat
    private lateinit var formatRp1: DecimalFormatSymbols
    private lateinit var kursIndonesia2: DecimalFormat
    private lateinit var formatRp2: DecimalFormatSymbols
    private var listCardAdapter: ListCardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        carData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("CAR_DATA", Car::class.java)!!
        } else {
            intent.getParcelableExtra("CAR_DATA")!!
        }

        paymentViewModel.carDataBuy = carData

        setUpPage()
        setUpListener()
        //setUpListCard()
    }

    private fun setUpPage() {
        lifecycleScope.launch {
            paymentViewModel.getDataProfile().collect {
                Glide.with(this@PaymentActivity)
                    .load(it.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.profPict)
            }
        }

        kursIndonesia1 = DecimalFormat.getCurrencyInstance() as DecimalFormat
        formatRp1 = DecimalFormatSymbols()

        formatRp1.currencySymbol = ""
        formatRp1.monetaryDecimalSeparator = ','
        formatRp1.groupingSeparator = '.'

        kursIndonesia1.decimalFormatSymbols = formatRp1

        binding.typeMerk.text = carData.carBrand
        binding.typeMerkChoose.text = carData.carBrand

        val price = kursIndonesia1.format(carData.price).split(",")[0]
        binding.pricePerDay.text = "${price} / Hari"
        binding.detail1.text = "$price / Hari x "

        binding.countResult.text = (1).toString()
        binding.dayCounts.text = (1).toString()

        val dialog = startLoadingDialog("Load payment", this@PaymentActivity)
        if(isNetworkAvailable(this@PaymentActivity)) {
            paymentViewModel.getListPaymentMethod().observe(this) {
                    result ->
                if (result != null) {
                    when(result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            dialog.dismiss()
                            if (ArrayList(result.data!!).isNotEmpty()) {
                                listDataTypePay = ArrayList(result.data!!)
                                setUpListCard()
                            } else {
                                showEmptyPaymentAlert()
                            }
                        }
                        is Resource.Error -> {
                            dialog.dismiss()
                            finish()
                            Toast.makeText(applicationContext, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else {
            finish()
            Toast.makeText(applicationContext, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpListener() {
        kursIndonesia2 = DecimalFormat.getCurrencyInstance() as DecimalFormat
        formatRp2 = DecimalFormatSymbols()

        formatRp2.currencySymbol = "Rp"
        formatRp2.monetaryDecimalSeparator = ','
        formatRp2.groupingSeparator = '.'

        kursIndonesia2.decimalFormatSymbols = formatRp2

        binding.backButton.setOnClickListener {
            finish()
        }

        lifecycleScope.launch {
            paymentViewModel.totalDay.collect {
                val price = kursIndonesia1.format(it * carData.price / 1000).split(",")[0]
                binding.dayCounts.text = it.toString()
                binding.countResult.text = it.toString()
                binding.totalPrice.text = "${price}k"
            }
        }

        lifecycleScope.launch {
            paymentViewModel.dataTypePay.collect {
                if (it.name == "NULL") {
                    binding.payUsedNumber.visibility = View.GONE
                    binding.payUsedValue.visibility = View.GONE
                    binding.payUsedLogo.visibility = View.GONE
                } else {
                    binding.payUsedNumber.visibility = View.VISIBLE
                    binding.payUsedValue.visibility = View.VISIBLE
                    binding.payUsedLogo.visibility = View.VISIBLE
                    val saldo = kursIndonesia2.format(it.value).split(",")[0]
                    binding.payUsedNumber.text = it.number
                    binding.payUsedValue.text = saldo
                    binding.payUsedLogo.setImageResource(setLogo(it.name))
                }
            }
        }

        lifecycleScope.launch {
            paymentViewModel.isEnough.collect {
                binding.warningValue.visibility = if (it) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
            }
        }

        binding.payNowButton.setOnClickListener {
            if (pinAttempt < 4) {
                if (binding.warningValue.visibility == View.VISIBLE) {
                    onSnackError("Mohon pilih metode pembayaran dan pastikan saldo cukup", binding.root, applicationContext)
                } else {
                    showPinConfirm()
                    pinAttempt++
                }
            } else {
                onSnackError("Pembayaran Gagal. Harap kembali dari laman ini dan coba kembali ", binding.root, applicationContext)
            }
        }

        binding.maxButton.setOnClickListener {
            if (paymentViewModel.totalDay.value < 30) {
                paymentViewModel.totalDay.value++
            }
        }

        binding.minButton.setOnClickListener {
            if (paymentViewModel.totalDay.value > 1) {
                paymentViewModel.totalDay.value--
            }
        }

        binding.selectedEWallet.setOnClickListener {
            showDialogEWallet()
        }
    }

    private fun setUpListCard() {
        val card = listDataTypePay.filter { it.type == "CARD" }

        if (card.isNotEmpty()) {
            binding.rvListCard.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rvListCard.setHasFixedSize(true)
            listCardAdapter = ListCardAdapter(ArrayList(card), -1)
            binding.rvListCard.adapter = listCardAdapter

            listCardAdapter?.setOnItemClickCallback(object :
                ListCardAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DataTypePay, adapterPos: Int) {
                    binding.borderSelectedEWallet.visibility = View.GONE
                    paymentViewModel.dataTypePay.value = data
                }

                override fun onItemDeleted(data: Int) {
                }

            })
        } else {
            binding.cardEmpty.visibility = View.VISIBLE
            binding.selectedEWallet.updateLayoutParams<ConstraintLayout.LayoutParams> {
                topToBottom = binding.cardEmpty.id
            }
        }
    }

    private fun showDialogEWallet() {
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(this@PaymentActivity)
        val inflater = this@PaymentActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_list_ewallet, null)

        builder.setView(dialogView)

        dialog = builder.create()
        dialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border_customed)
        //dialog.window?.setLayout(600, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)

        val rvAdapter = dialogView.findViewById<RecyclerView>(R.id.rv_list_ewallet)
        val emptyLayout = dialogView.findViewById<LinearLayout>(R.id.error_message)
        val buttonBack = dialogView.findViewById<CardView>(R.id.back_button_e)
        emptyLayout.visibility = View.GONE

        val window = dialog.window
        val wlp: WindowManager.LayoutParams = window?.attributes!!

        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp

        val ewallet = listDataTypePay.filter { it.type == "EWALLET" }

        if (ewallet.isNotEmpty()) {
            rvAdapter.layoutManager =
                LinearLayoutManager(this)
            rvAdapter.setHasFixedSize(true)
            val listEWalletAdapter = ListEWalletAdapter(ArrayList(ewallet))
            rvAdapter.adapter = listEWalletAdapter

            listEWalletAdapter.setOnItemClickCallback(object :
                ListEWalletAdapter.OnItemClickCallback {
                override fun onItemClicked(data: DataTypePay) {
                    if (listCardAdapter != null) {
                        listCardAdapter?.changedCardToEWallet()
                    }
                    binding.borderSelectedEWallet.visibility = View.VISIBLE
                    binding.selectedEWalletBalance.text = "Saldo ${kursIndonesia2.format(data.value).split(",")[0]}"
                    binding.logoSelectedEWallet.setImageResource(setLogo(data.name))
                    paymentViewModel.dataTypePay.value = data
                    dialog.dismiss()
                }

                override fun onItemDeleted(data: Int) {}
            })
        } else {
            rvAdapter.visibility = View.GONE
            emptyLayout.visibility = View.VISIBLE
        }

        buttonBack.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun checkPinAndProcessing(PIN: String) {
        fun processingPayment(loadDialog: AlertDialog) {
            paymentViewModel.addOrder().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            loadDialog.dismiss()
                            val intent =
                                Intent(this@PaymentActivity, FinishPaymentActivity::class.java)
                            intent.putExtra("ORDER_DATA", result.data)
                            startActivity(intent)
                        }
                        is Resource.Error -> {
                            loadDialog.dismiss()
                            if (result.message == "EMPTY") {
                                finish()
                                Toast.makeText(this, "Mohon maaf mobil tidak tersedia saat ini", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        }

        if (isNetworkAvailable(this@PaymentActivity)) {
            val loadDialog = startLoadingDialog("Sedang memproses...", this)
            paymentViewModel.getPIN().observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Success -> {
                            if (PIN == result.data) {
                                Log.d("PaymentActivity", "SUCCESS")
                                loadDialog.dismiss()
                                processingPayment(loadDialog)
                            } else {

                                loadDialog.dismiss()
                                if (pinAttempt < 4) {
                                    val dialogView = showPinConfirm()
                                    onSnackErrorDialog("PIN Salah, coba kembali", dialogView)
                                    pinAttempt++
                                } else {
                                    onSnackError("Pembayaran Gagal. Harap kembali dari laman ini dan coba kembali ", binding.root, applicationContext)
                                }
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(this, "Error: ${result.message}", Toast.LENGTH_LONG)
                                .show()
                            loadDialog.dismiss()
                        }
                        is Resource.Loading -> {

                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, applicationContext)
        }
    }

    private fun showPinConfirm(): View {
        var PIN_FIRST: String? = null
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

        title.text = "Konfirmasi PIN"
        subTitle.text = "Masukkan PIN untuk melanjutkan transaksi"

        customDialog.show()

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
                PIN_FIRST = AESEncryption.encrypt(text.toString().trim())
            }
        }

        okButton.setOnClickListener {
            if (!pinEmpty) {
                checkPinAndProcessing(PIN_FIRST!!)
                customDialog.dismiss()
            } else {
                errorMessage.text = "Harap isi PIN"
                errorMessage.alpha = 1F
                okButton.isClickable = false
            }
        }

        cancelButton.setOnClickListener {
            customDialog.dismiss()
        }

        return dialogView
    }

    private fun onSnackErrorDialog(errorMessage: String, dialogView: View) {
        val snackbar = Snackbar.make(
            this@PaymentActivity, dialogView, convertCode(errorMessage),
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        val snackbarView = snackbar.view

        val textView =
            snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        val typeface = ResourcesCompat.getFont(applicationContext, R.font.montserrat_medium)
        textView.typeface = typeface
        textView.textSize = 12f

        val snackbarLayout = snackbar.view
        val layoutParams = snackbarLayout.layoutParams as FrameLayout.LayoutParams
        layoutParams.setMargins(32, 0, 32, 64)
        snackbarLayout.layoutParams = layoutParams

        snackbar.show()
    }

    private fun showEmptyPaymentAlert() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_empty_payment_method, null)
        val customDialog = AlertDialog.Builder(this).setView(dialogView).create()
        customDialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        val btDismiss = dialogView.findViewById<TextView>(R.id.ok_button)
        customDialog.window?.setLayout(850, WindowManager.LayoutParams.WRAP_CONTENT)
        customDialog.setCanceledOnTouchOutside(false)
        customDialog.setCancelable(false)
        btDismiss.setOnClickListener {
            customDialog.dismiss()
            finish()
        }
        customDialog.show()
    }
}