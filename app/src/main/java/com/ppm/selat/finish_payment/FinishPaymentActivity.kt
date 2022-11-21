package com.ppm.selat.finish_payment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ppm.selat.R
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.utils.setLogo
import com.ppm.selat.databinding.ActivityFinishPaymentBinding
import com.ppm.selat.home.HomeActivity
import com.ppm.selat.transaction.TransactionActivity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.random.Random

class FinishPaymentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishPaymentBinding
    private lateinit var orderData: OrderData
    private lateinit var nameProfile: String

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

        nameProfile = intent.getStringExtra("NAME_PROFILE")!!

        setUpPage()
        setNotification()
    }

    private fun setUpPage() {
        val kursIndonesia: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()

        formatRp.currencySymbol = "Rp"
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp

        val date = getDate(orderData.dateOrder)
        with(binding) {
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
            logoPayment.setImageResource(setLogo(orderData.paymentTypeName.split(" ++ ")[1]))
        }
    }

    private fun getDate(date: String): List<String> {
        val splitDate = date.split(" ")
        return listOf(
            "${splitDate[0]} ${splitDate[1]} ${splitDate[2]}",
            "${splitDate[3]} ${splitDate[4]}"
        )
    }

    private fun setNotification() {
        createNotificationDetail()

        val mainIntent = Intent(this, TransactionActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val mainPendingIntent =
            PendingIntent.getActivity(this, 1, mainIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, "channel01")
        //notificationBuilder.setSmallIcon(R.drawable.logo_selat)
        notificationBuilder.setContentTitle("Selat - Transaksi Berhasil!")
        notificationBuilder.setContentText("Hai, ${nameProfile}, Pesanan sewa anda dengan Order No ${orderData.id} telah diterima. Ketuk untuk melihat daftar transaksi terkini")
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        notificationBuilder.setAutoCancel(false)
        notificationBuilder.setContentIntent(mainPendingIntent)
        notificationBuilder.setStyle(
            NotificationCompat.BigTextStyle().setBigContentTitle("Selat - Transaksi Berhasil!")
                .bigText("Hai, ${nameProfile}, Pesanan sewa anda dengan Order No ${orderData.id} telah diterima. Ketuk untuk melihat daftar transaksi terkini")
        )
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(Random.nextInt(), notificationBuilder.build())
    }

    private fun createNotificationDetail() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "NotifikasiTransaksi"
            val description = "Notifikasi setelah traansaksi berhasil"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel("channel01", name, importance)
            notificationChannel.description = description
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}