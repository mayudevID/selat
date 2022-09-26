package com.ppm.selat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.Fade
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.auth.LoginActivity
import org.imaginativeworld.whynotimagecarousel.CarouselItem
import org.imaginativeworld.whynotimagecarousel.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.jetbrains.annotations.Nullable

class MainActivity : AppCompatActivity() {
    private lateinit var indicator1: View
    private lateinit var indicator2: View
    private lateinit var indicator3: View
    private lateinit var indicator4: View
    private lateinit var textNext: TextView
    private lateinit var buttonNextToLogin: FrameLayout
    private lateinit var carousel: ImageCarousel
    private lateinit var skipButton: TextView
    var isNextToLogin: Boolean = false

    private val images = arrayListOf(R.drawable.onboard_1,
        R.drawable.onboard_2, R.drawable.onboard_3, R.drawable.onboard_4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

        setUpIndicatorText()
        setUpLayout()
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
                        indicator4.setBackgroundResource(R.drawable.indicator_white)
                        indicator1.setBackgroundResource(R.drawable.indicator_yellow)
                        indicator2.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = false
                    }
                    1 -> {
                        indicator1.setBackgroundResource(R.drawable.indicator_white)
                        indicator2.setBackgroundResource(R.drawable.indicator_yellow)
                        indicator3.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = false
                    }
                    2 -> {
                        indicator2.setBackgroundResource(R.drawable.indicator_white)
                        indicator3.setBackgroundResource(R.drawable.indicator_yellow)
                        indicator4.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = false
                    }
                    3 -> {
                        indicator3.setBackgroundResource(R.drawable.indicator_white)
                        indicator4.setBackgroundResource(R.drawable.indicator_yellow)
                        indicator1.setBackgroundResource(R.drawable.indicator_white)
                        isNextToLogin = true
                    }
                }

                if (position != 3) {
                    "Lanjut".also { textNext.text = it }
                } else {
                    "Mulai Menjelajah".also { textNext.text = it }
                }
            }
        }
    }

    private fun setUpIndicatorText() {
        indicator1 = findViewById(R.id.id_1)
        indicator2 = findViewById(R.id.id_2)
        indicator3 = findViewById(R.id.id_3)
        indicator4 = findViewById(R.id.id_4)
    }

    private fun setUpLayout() {
        textNext = findViewById(R.id.text_next)
        skipButton = findViewById(R.id.skip)
        buttonNextToLogin = findViewById(R.id.button_next_to_login)
    }

    private fun setUpButtonListener() {

        fun goToLoginActivity() {
            val intent = Intent(this, LoginActivity::class.java)
            //ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
            startActivity(intent)
            finish()
        }

        buttonNextToLogin.setOnClickListener {
            if (!isNextToLogin) {
                carousel.next()
            } else {
                goToLoginActivity()
            }
        }

        skipButton.setOnClickListener {
            goToLoginActivity()
        }
    }
}