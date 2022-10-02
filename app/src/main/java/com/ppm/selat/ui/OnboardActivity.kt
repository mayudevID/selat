package com.ppm.selat.ui

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.R
import com.ppm.selat.ui.auth.LoginActivity
import com.ppm.selat.databinding.ActivityOnboardBinding
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.jetbrains.annotations.Nullable

class OnboardActivity : AppCompatActivity() {

    private lateinit var carousel: ImageCarousel
    private lateinit var binding: ActivityOnboardBinding
    var locCarousel: Int = 0
    var lastLocCarousel: Int = 0

    private val images = arrayListOf(
        R.drawable.onboard_1,
        R.drawable.onboard_2, R.drawable.onboard_3, R.drawable.onboard_4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSwitch.setFactory {
            val textView = TextView(this)
            textView.setTextColor(Color.BLACK)
            textView.typeface = ResourcesCompat.getFont(this, R.font.montserrat_bold)
            textView.textSize = 16F
            textView.text = "Lanjut"
            textView.gravity = Gravity.CENTER
            textView
        }

        binding.textSwitch.setInAnimation(this, android.R.anim.fade_in);
        binding.textSwitch.setOutAnimation(this, android.R.anim.fade_out);

        supportActionBar?.hide()

        carousel = findViewById(R.id.carousel)
        val list = mutableListOf<CarouselItem>()

        for (data in images) {
            list.add(
                CarouselItem(
                    imageDrawable = data
                )
            )
        }

        carousel.addData(list)
        setUpButtonListener()

        carousel.onScrollListener = object : CarouselOnScrollListener {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int,
                position: Int,
                @Nullable carouselItem: CarouselItem?
            ) {
                when (position) {
                    0 -> {
                        binding.id4.setBackgroundResource(R.drawable.indicator_white)
                        binding.id1.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id2.setBackgroundResource(R.drawable.indicator_white)
                        lastLocCarousel = locCarousel
                        locCarousel = position
                    }
                    1 -> {
                        binding.id1.setBackgroundResource(R.drawable.indicator_white)
                        binding.id2.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id3.setBackgroundResource(R.drawable.indicator_white)
                        lastLocCarousel = locCarousel
                        locCarousel = position
                    }
                    2 -> {
                        binding.id2.setBackgroundResource(R.drawable.indicator_white)
                        binding.id3.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id4.setBackgroundResource(R.drawable.indicator_white)
                        lastLocCarousel = locCarousel
                        locCarousel = position
                    }
                    3 -> {
                        binding.id3.setBackgroundResource(R.drawable.indicator_white)
                        binding.id4.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id1.setBackgroundResource(R.drawable.indicator_white)
                        lastLocCarousel = locCarousel
                        locCarousel = position
                    }
                }

                if (position == 3) {
                    binding.textSwitch.setText("Mulai Jelajah")
                } else {
                    if (lastLocCarousel == 3) {
                        binding.textSwitch.setText("Lanjut")
                    }
                }
            }
        }
    }

    private fun setUpButtonListener() {
        fun goToLoginActivity() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonNext.setOnClickListener {
            if (locCarousel != 3) {
                carousel.next()
            } else {
                goToLoginActivity()
            }
        }

        binding.skipButton.setOnClickListener {
            goToLoginActivity()
        }
    }
}
