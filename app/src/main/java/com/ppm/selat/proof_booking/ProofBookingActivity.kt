package com.ppm.selat.proof_booking

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfName.Document
import com.itextpdf.kernel.pdf.PdfWriter
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.databinding.ActivityProofBookingBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ProofBookingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProofBookingBinding
    private lateinit var orderData: OrderData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProofBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        orderData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("ORDER_DATA", OrderData::class.java)!!
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("ORDER_DATA")!!
        }

        setUpListener()
    }

    private fun setUpListener() {
        val kursIndonesia1 = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp1 = DecimalFormatSymbols()

        formatRp1.currencySymbol = ""
        formatRp1.monetaryDecimalSeparator = ','
        formatRp1.groupingSeparator = '.'

        kursIndonesia1.decimalFormatSymbols = formatRp1

        val priceTotal = kursIndonesia1.format(orderData.rentDays * orderData.price).split(",")[0]
        val listPaymentDetail = orderData.paymentTypeName.split(" ++ ")

        with(binding) {
            binding.backButtonBp.setOnClickListener {
                finish()
            }
            tanggal.text = orderData.dateOrder
            orderNum.text = orderData.id
            brandTarget.text = orderData.brand
            manufacturerTarget.text = orderData.manufacturer
            rentDaysTarget.text = "${orderData.rentDays} Hari"
            priceTotalTarget.text = priceTotal
            priceTotalTarget2.text = priceTotal
            orderNumAccount.text =
                "Bayar Menggunakan\n${orderData.paymentNumber}\n${listPaymentDetail[1]}\n(${listPaymentDetail[0]})"

        }
    }

    private fun createPdf() {
//        try {
//            val pdfPath =
//                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    .toString()
//            val file = File(pdfPath, "invoice_selat")
//            val outputStream = FileOutputStream(file)
//
//            val writer = PdfWriter(file)
//            val pdfDocument = PdfDocument(writer)
//            val document = Document
//
//        } catch (e: FileNotFoundException) {
//
//        }
    }
}