package com.ppm.selat.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.databinding.ActivityHomeBinding
import com.ppm.selat.model.Car
import com.ppm.selat.ui.detail_car.DetailCarActivity
import com.ppm.selat.ui.detail_car.DetailCarActivity.Companion.CAR_DATA


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listSedanAdapter: ListSedanAdapter
    private lateinit var listSuvAdapter: ListSuvAdapter
    private lateinit var listCarBrandsAdapter: ListCarBrandsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpListCar()
        setUpListener()
    }

    private fun setUpListCar() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListCarBrands.layoutManager = linearLayoutManager
        binding.rvListCarBrands.setHasFixedSize(true)

        val listBrands = arrayListOf<String>("Toyota", "Honda", "Suzuki", "Nissan", "Hyundai")

        listCarBrandsAdapter = ListCarBrandsAdapter(listBrands)
        binding.rvListCarBrands.adapter = listCarBrandsAdapter

        //////////////////////////////////////////////////////

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

        //////////////////////////////////////////////////////

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
                val intent = Intent(this@HomeActivity, DetailCarActivity::class.java)
                intent.putExtra("CAR_DATA", data)
                startActivity(intent)
            }

            override fun onItemDeleted(data: Car) {
            }
        })

        listSuvAdapter.setOnItemClickCallback(object : ListSuvAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Car) {
                val intent = Intent(applicationContext, DetailCarActivity::class.java)
                intent.putExtra("CAR_DATA", data)
                startActivity(intent)
            }

            override fun onItemDeleted(data: Car) {
            }
        })

        listCarBrandsAdapter.setOnItemClickCallback(object : ListCarBrandsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {

            }

            override fun onItemDeleted(data: String) {
            }
        })
    }
}