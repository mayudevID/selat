package com.ppm.selat.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    var isNextToLogin: Boolean = false

    private val images = arrayListOf(
        R.drawable.onboard_1,
        R.drawable.onboard_2, R.drawable.onboard_3, R.drawable.onboard_4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                when (position){
                    0 -> {
                        binding.id4.setBackgroundResource(R.drawable.indicator_white)
                        binding.id1.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id2.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = false
                    }
                    1 -> {
                        binding.id1.setBackgroundResource(R.drawable.indicator_white)
                        binding.id2.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id3.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = false
                    }
                    2 -> {
                        binding.id2.setBackgroundResource(R.drawable.indicator_white)
                        binding.id3.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id4.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = false
                    }
                    3 -> {
                        binding.id3.setBackgroundResource(R.drawable.indicator_white)
                        binding.id4.setBackgroundResource(R.drawable.indicator_yellow)
                        binding.id1.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = true
                    }
                }

                if (position != 3) {
                    "Lanjut".also { binding.textNext.text = it }
                } else {
                    "Mulai Menjelajah".also { binding.textNext.text = it }
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

        binding.textNext.setOnClickListener {
            if (!isNextToLogin) {
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