package com.ppm.selat.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ppm.selat.Manufacturer
import com.ppm.selat.TypeCar
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.ui.home.ListCarManufacturerAdapter
import com.ppm.selat.core.ui.home.ListSedanAdapter
import com.ppm.selat.core.ui.home.ListSuvAdapter
import com.ppm.selat.databinding.ActivityHomeBinding
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.detail_car.DetailCarActivity
import com.ppm.selat.profile.ProfileActivity
import com.ppm.selat.putExtra
import com.ppm.selat.ui.pick_car.PickCarActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModel()

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listSedanAdapter: ListSedanAdapter
    private lateinit var listSuvAdapter: ListSuvAdapter
    private lateinit var listCarManufacturerAdapter: ListCarManufacturerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpProfile()
        setUpListCar()
        Handler(Looper.getMainLooper()).postDelayed({
            setUpAnimation()
        }, 10)
    }

    private fun setUpProfile() {
        homeViewModel.userDataStream.observe(this) {
            binding.userName.text = it.name
            Glide.with(this)
                .load(it.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.profileImage)
        }
    }

    private fun setUpListCar() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListCarBrands.layoutManager = linearLayoutManager
        binding.rvListCarBrands.setHasFixedSize(true)

        val listBrands = arrayListOf(
            "Toyota",
            "Honda",
            "Suzuki",
            "Nissan",
            "Hyundai",
        )

        listCarManufacturerAdapter = ListCarManufacturerAdapter(listBrands)
        binding.rvListCarBrands.adapter = listCarManufacturerAdapter

        val sedanLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListSedan.layoutManager = sedanLayoutManager
        binding.rvListSedan.setHasFixedSize(true)

        val suvLayoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListSuv.layoutManager = suvLayoutManager
        binding.rvListSuv.setHasFixedSize(true)

        homeViewModel.getAllCars.observe(this) { result ->
            if (result != null) {
                when(result) {
                    is Resource.Success -> {
                        val dataCar = result.data!!
                        val sedanData = ArrayList<Car>()
                        val suvData = ArrayList<Car>()
                        for (data in dataCar) {
                            if (data.typeCar == "SEDAN") {
                                sedanData.add(data)
                            } else {
                                suvData.add(data)
                            }
                        }
                        binding.suvText.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            topToBottom = binding.rvListSedan.id
                            topMargin = 30
                        }
                        binding.loadSedanCar.visibility = View.GONE
                        binding.loadSuvCar.visibility = View.GONE
                        binding.errorMessage.visibility = View.GONE
                        if (sedanData.isEmpty()) {

                        } else {
                            listSedanAdapter = ListSedanAdapter(sedanData)
                            binding.rvListSedan.adapter = listSedanAdapter
                            binding.rvListSedan.visibility = View.VISIBLE
                        }
                        if (suvData.isEmpty()) {

                        } else {
                            listSuvAdapter = ListSuvAdapter(suvData)
                            binding.rvListSuv.adapter = listSuvAdapter
                            binding.rvListSuv.visibility = View.VISIBLE
                        }
                        setUpListener()
                    }
                    is Resource.Loading -> {
                        binding.rvListSedan.visibility = View.GONE
                        binding.rvListSuv.visibility = View.GONE
                        binding.errorMessage.visibility = View.GONE
                        binding.loadSedanCar.visibility = View.VISIBLE
                        binding.loadSuvCar.visibility = View.VISIBLE
                        binding.suvText.updateLayoutParams<ConstraintLayout.LayoutParams> {
                            topToBottom = binding.loadSedanCar.id
                            topMargin = 64
                        }
                    }
                    is Resource.Error -> {
                        binding.errorMessage.visibility = View.VISIBLE
                    }
                }
            }

        }
    }

    private fun setUpListener() {
        binding.profileBanner.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


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
                val intent = Intent(this@HomeActivity, DetailCarActivity::class.java)
                intent.putExtra("CAR_DATA", data)
                startActivity(intent)
            }

            override fun onItemDeleted(data: Car) {
            }
        })

        listCarManufacturerAdapter.setOnItemClickCallback(object :
            ListCarManufacturerAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {
                val intent = Intent(this@HomeActivity, PickCarActivity::class.java)
                intent.putExtra(convertStringToManufacturer(data))
                intent.putExtra(TypeCar.ALL)
                startActivity(intent)
            }

            override fun onItemDeleted(data: String) {
            }
        })

        binding.expandSedan.setOnClickListener {
            val intent = Intent(this, PickCarActivity::class.java)
            intent.putExtra(Manufacturer.ALL)
            intent.putExtra(TypeCar.SEDAN)
            startActivity(intent)
        }

        binding.expandSuv.setOnClickListener {
            val intent = Intent(this, PickCarActivity::class.java)
            intent.putExtra(Manufacturer.ALL)
            intent.putExtra(TypeCar.SUV)
            startActivity(intent)
        }
    }

    private fun convertStringToManufacturer(data: String) : Manufacturer{
        return when (data) {
            "Toyota" -> Manufacturer.TOYOTA
            "Hyundai" -> Manufacturer.HYUNDAI
            "Suzuki" -> Manufacturer.SUZUKI
            "Nissan" -> Manufacturer.NISSAN
            "Honda" -> Manufacturer.HONDA
            else -> Manufacturer.ALL
        }
    }

    private fun setUpAnimation() {
        val search = ObjectAnimator.ofFloat(binding.searchBar, View.ALPHA, 1F).setDuration(100)
        val profileBanner =
            ObjectAnimator.ofFloat(binding.profileBanner, View.ALPHA, 1F).setDuration(100)
        val rvListCarBrands =
            ObjectAnimator.ofFloat(binding.rvListCarBrands, View.ALPHA, 1F).setDuration(100)
        val sedanText = ObjectAnimator.ofFloat(binding.sedanText, View.ALPHA, 1F).setDuration(100)
        val suvText = ObjectAnimator.ofFloat(binding.suvText, View.ALPHA, 1F).setDuration(100)
        val expandSedan =
            ObjectAnimator.ofFloat(binding.expandSedan, View.ALPHA, 1F).setDuration(100)
        val expandSuv = ObjectAnimator.ofFloat(binding.expandSuv, View.ALPHA, 1F).setDuration(100)
        val rvListSedan =
            ObjectAnimator.ofFloat(binding.rvListSedan, View.ALPHA, 1F).setDuration(100)
        val rvListSuv = ObjectAnimator.ofFloat(binding.rvListSuv, View.ALPHA, 1F).setDuration(100)
        val loadSuv = ObjectAnimator.ofFloat(binding.loadSuvCar, View.ALPHA, 1F).setDuration(100)
        val loadSedan =
            ObjectAnimator.ofFloat(binding.loadSedanCar, View.ALPHA, 1F).setDuration(100)
        val errorLayout = ObjectAnimator.ofFloat(binding.errorMessage, View.ALPHA, 1F).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(sedanText, expandSedan)
        }

        val togetherSecond = AnimatorSet().apply {
            playTogether(suvText, expandSuv)
        }

        val loadSedanTogether = AnimatorSet().apply {
            playTogether(rvListSedan, loadSedan, errorLayout)
        }

        val loadSuvTogether = AnimatorSet().apply {
            playTogether(rvListSuv, loadSuv)
        }

        AnimatorSet().apply {
            playSequentially(
                search,
                profileBanner,
                rvListCarBrands,
                together,
                loadSedanTogether,
                togetherSecond,
                loadSuvTogether,
            )
            start()
        }
    }
}