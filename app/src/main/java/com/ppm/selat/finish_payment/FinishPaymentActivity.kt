package com.ppm.selat.finish_payment

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ppm.selat.R
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.databinding.ActivityFinishPaymentBinding
import com.ppm.selat.home.HomeActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class FinishPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishPaymentBinding
    private lateinit var orderData : OrderData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        orderData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("ORDER_DATA", OrderData::class.java)!!
        } else {
            intent.getParcelableExtra("ORDER_DATA")!!
        }

        setUpPage()
    }

    private fun setUpPage() {
        val kursIndonesia: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()

        formatRp.currencySymbol = "Rp"
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp

        val date = getDate(orderData.dateOrder)
        with (binding) {
            orderNum.text = "Order No ${orderData.id}"
            dateOrder.text = date[0]
            timeOrder.text = date[1]
            noRek.text = orderData.paymentNumber
            payTotal.text = kursIndonesia.format(orderData.price * orderData.rentDays).split(",")[0]
            finishButton.setOnClickListener {
                val intent = Intent(this@FinishPaymentActivity, HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
            when(orderData.paymentTypeName) {
                "MASTERCARD" -> {
                    logoPayment.setImageResource(com.ppm.selat.core.R.drawable.mastercard_logo)
                }
                "GOPAY" -> {
                    logoPayment.setImageResource(com.ppm.selat.core.R.drawable.gopay_logo)
                }
            }
        }
    }

    private fun getDate(date: String) : List<String>{
        val splitDate = date.split(" ")
        return listOf("${splitDate[0]} ${splitDate[1]} ${splitDate[2]}", "${splitDate[3]} ${splitDate[4]}")
    }
}