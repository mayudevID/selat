package com.ppm.selat.detail_car

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.databinding.ActivityDetailCarBinding
import com.ppm.selat.location_car.LocationCarActivity
import com.ppm.selat.payment.PaymentActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class DetailCarActivity : AppCompatActivity() {

    private val detailCarViewModel: DetailCarViewModel by viewModel()
    private lateinit var binding: ActivityDetailCarBinding
    private lateinit var carData: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.payButton.isClickable = false
        binding.payButton.alpha = 0.5F

        carData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("CAR_DATA", Car::class.java)!!
        } else {
            intent.getParcelableExtra("CAR_DATA")!!
        }

        setUpListener()
        setUpPage()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.motionTop.transitionToEnd()
        }, 50)

        //BackSystemPressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backFromPage()
            }
        })
    }

    private fun setUpPage() {
        val kursIndonesia: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()

        formatRp.currencySymbol = "Rp"
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp

        with(binding) {
            Glide.with(this@DetailCarActivity)
                .load(carData.carImage.primaryPhoto)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadBigImageCar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.loadBigImageCar.visibility = View.GONE
                        return false
                    }

                })
                .into(this.bigImageCar)
            typeBrand.text = carData.carBrand
            yearProduction.text = "Th ${carData.yearProduction}"
            priceData.text = kursIndonesia.format(carData.price)
            desc1.text = carData.spec.a
            desc2.text = carData.spec.b
            desc3.text = carData.spec.c
            desc4.text = carData.location

            if (carData.carManufacturer == "Toyota") {
                logoBrand.setImageResource(R.drawable.toyota_detail_logo)
            } else if (carData.carManufacturer == "Hyundai") {
                logoBrand.setImageResource(R.drawable.hyundai_detail_logo)
            } else if (carData.carManufacturer == "Honda") {
                logoBrand.setImageResource(R.drawable.honda_detail_logo)
            } else if (carData.carManufacturer == "Suzuki") {
                logoBrand.setImageResource(R.drawable.suzuki_detail_logo)
            } else if (carData.carManufacturer == "Nissan") {
                logoBrand.setImageResource(R.drawable.nissan_only_logo)
            }

            detailCarViewModel.getAvailableCar(carData.id).observe(this@DetailCarActivity) {
                result ->
                if (result != null) {
                    if (result > 0)   {
                        binding.payButton.alpha = 1F
                        availableStatus.text = "Tersedia - $result"
                        availableStatus.setTextColor(Color.parseColor("#228C22"))
                        payButton.isClickable = true
                    } else {
                        binding.payButton.alpha = 0.4F
                        availableStatus.text = "Kosong - $result"
                        availableStatus.setTextColor(Color.parseColor("#FF0000"))
                        payButton.isClickable = false
                    }
                }
            }
        }
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            backFromPage()
        }

        binding.mapsDisplay.setOnClickListener {
            val intent = Intent(this, LocationCarActivity::class.java)
            intent.putExtra("CAR_DATA", carData)
            startActivity(intent)
        }

        binding.payButton.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            intent.putExtra("CAR_DATA", carData)
            startActivity(intent)
        }
    }

    private fun backFromPage() {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.motionTop.transitionToStart()
        }, 0)
        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, 100)
    }
}