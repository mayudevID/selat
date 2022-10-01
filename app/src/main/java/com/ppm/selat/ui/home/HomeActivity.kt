package com.ppm.selat.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.databinding.ActivityHomeBinding
import com.ppm.selat.model.Car


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listSedanAdapter: ListSedanAdapter
    private lateinit var listSuvAdapter: ListSuvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListCar()
        setUpListener()
    }

    private fun setUpListCar() {
        val sedanLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListSedan.layoutManager = sedanLayoutManager
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

        listSedanAdapter = ListSedanAdapter(listSedan)
        binding.rvListSedan.adapter = listSedanAdapter

            val suvLayoutManager =
        LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListSuv.layoutManager = suvLayoutManager
        binding.rvListSuv.setHasFixedSize(true)

        val suvData = Car(
            id = null,
            typeCar = "SUV",
            carImage = null,
            carName = "Avanza",
            price = 200,
            rating = 5.0
        )

        val listSuv = ArrayList<Car>()
        listSuv.addAll(listOf(suvData, suvData, suvData))

        listSuvAdapter = ListSuvAdapter(listSuv)
        binding.rvListSuv.adapter = listSuvAdapter
    }

    private fun setUpListener() {
        listSedanAdapter.setOnItemClickCallback(object : ListSedanAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Car) {

            }

            override fun onItemDeleted(data: Car) {
            }
        })

        listSuvAdapter.setOnItemClickCallback(object : ListSuvAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Car) {

            }

            override fun onItemDeleted(data: Car) {
            }
        })
    }
}