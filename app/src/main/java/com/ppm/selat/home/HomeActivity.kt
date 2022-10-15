package com.ppm.selat.home

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ppm.selat.MyApplication
import com.ppm.selat.ViewModelFactory
import com.ppm.selat.core.presentation.home.ListCarBrandsAdapter
import com.ppm.selat.core.presentation.home.ListSedanAdapter
import com.ppm.selat.core.presentation.home.ListSuvAdapter
import com.ppm.selat.databinding.ActivityHomeBinding
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.detail_car.DetailCarActivity
import com.ppm.selat.profile.ProfileActivity
import com.ppm.selat.ui.pick_car.PickCarActivity
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: ViewModelFactory

    private val homeViewModel: HomeViewModel by viewModels {
        factory
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var listSedanAdapter: ListSedanAdapter
    private lateinit var listSuvAdapter: ListSuvAdapter
    private lateinit var listCarBrandsAdapter: ListCarBrandsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setUpProfile()
        setUpListCar()
        setUpListener()
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
                .into(binding.profileImage);
        }
    }

    private fun setUpListCar() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvListCarBrands.layoutManager = linearLayoutManager
        binding.rvListCarBrands.setHasFixedSize(true)

        val listBrands = arrayListOf("Toyota", "Honda", "Suzuki", "Nissan", "Hyundai")

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
            rating = 5.0,
            yearProduction = 2022
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
            rating = 5.0,
            yearProduction = 2022
        )

        val listSuv = ArrayList<Car>()
        listSuv.addAll(listOf(suvData, suvData, suvData))

        listSuvAdapter = ListSuvAdapter(listSuv)
        binding.rvListSuv.adapter = listSuvAdapter
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
                val intent = Intent(applicationContext, DetailCarActivity::class.java)
                intent.putExtra("CAR_DATA", data)
                startActivity(intent)
            }

            override fun onItemDeleted(data: Car) {
            }
        })

        listCarBrandsAdapter.setOnItemClickCallback(object :
            ListCarBrandsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: String) {

            }

            override fun onItemDeleted(data: String) {
            }
        })

        binding.expandSedan.setOnClickListener {
            val intent = Intent(this, PickCarActivity::class.java)
            startActivity(intent)
        }

        binding.expandSuv.setOnClickListener {
            val intent = Intent(this, PickCarActivity::class.java)
            startActivity(intent)
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

        val together = AnimatorSet().apply {
            playTogether(sedanText, expandSedan)
        }

        val togetherSecond = AnimatorSet().apply {
            playTogether(suvText, expandSuv)
        }

        AnimatorSet().apply {
            playSequentially(
                search,
                profileBanner,
                rvListCarBrands,
                together,
                rvListSedan,
                togetherSecond,
                rvListSuv,
            )
            start()
        }
    }
}