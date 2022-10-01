package com.ppm.selat.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.databinding.ActivityHomeBinding
import com.ppm.selat.model.Car


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListCar()
        setUpListener()
    }

    private fun setUpListCar() {
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListSedan.layoutManager = layoutManager
        binding.rvListSedan.setHasFixedSize(true)

        val sedanData = Car(
            id = null,
            typeCar = "SEDAN",
            carImage = null,
            carName = "Camry",
            price = 200,
            rating = 5.0
        )

        val listSedan = ArrayList<Car>()
        listSedan.addAll(listOf(sedanData, sedanData, sedanData))

        binding.rvListSedan.adapter = ListSedanAdapter(listSedan)
    }

    private fun setUpListener() {

    }
}