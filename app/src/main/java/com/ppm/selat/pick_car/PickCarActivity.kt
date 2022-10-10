package com.ppm.selat.ui.pick_car

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.R
import com.ppm.selat.core.presentation.pick_car.ListAvailableCarAdapter
import com.ppm.selat.core.presentation.pick_car.ListBrandsCarToPickAdapter
import com.ppm.selat.databinding.ActivityPickCarBinding
import com.ppm.selat.core.domain.model.Car


class PickCarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPickCarBinding
    private lateinit var listAvailableCarAdapter: ListAvailableCarAdapter
    private lateinit var listBrandsCarToPickAdapter: ListBrandsCarToPickAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setAppBarTitle()
        setUpAdapter()
        setUpListener()
    }

    private fun setAppBarTitle() {
//        val customFont = ResourcesCompat.getFont(this@PickCarActivity, R.font.montserrat_semibold);
//        binding.collapisngToolbar.setCollapsedTitleTextColor(Color.BLACK)
//        binding.collapisngToolbar.setExpandedTitleColor(Color.BLACK)
//        binding.collapisngToolbar.setCollapsedTitleTypeface(customFont)
//        binding.collapisngToolbar.setExpandedTitleTypeface(customFont)

        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener { barLayout, verticalOffset ->
            Log.d("PickCarActivity", "vo: $verticalOffset")

            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }

            Log.d("PickCarActivity", "sr: $scrollRange")

            if (scrollRange + verticalOffset == 0) {
                binding.collapisngToolbar.title = "Mobil yang tersedia"
                isShow = true
            } else if (isShow) {
                binding.collapisngToolbar.title =
                    " " //careful there should a space between double quote otherwise it wont work
                isShow = false
            }
        }


    }

    private fun setUpListener() {
        listBrandsCarToPickAdapter.setOnItemClickCallback(object :
            ListBrandsCarToPickAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Int) {

            }

            override fun onItemDeleted(data: Int) {

            }

        })

        listAvailableCarAdapter.setOnItemClickCallback(object :
            ListAvailableCarAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Car) {

            }

            override fun onItemDeleted(data: Car) {

            }
        })

        binding.backButtonFromPick.setOnClickListener {
            finish()
        }
    }

    private fun setUpAdapter() {
        binding.rvListBrandsCarToPick.setHasFixedSize(true)
        binding.rvListBrandsCarToPick.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false,
        )

        val listImage = arrayListOf<Int>(
            R.drawable.all_brands_pick,
            R.drawable.toyota_only_logo,
            R.drawable.honda_only_logo,
            R.drawable.nissan_only_logo,
        )

        listBrandsCarToPickAdapter = ListBrandsCarToPickAdapter(listImage)
        binding.rvListBrandsCarToPick.adapter = listBrandsCarToPickAdapter

        //////////////////////////////////////////////////////

        binding.rvListAvailableCarToPick.setHasFixedSize(false)
        binding.rvListAvailableCarToPick.layoutManager = LinearLayoutManager(this)

        val listData = arrayListOf<Car>(
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
            Car(
                id = null,
                carImage = null,
                typeCar = null,
                carName = "Fortuner",
                yearProduction = 2022,
                price = 200,
                rating = 4.8
            ),
        )

        listAvailableCarAdapter = ListAvailableCarAdapter(listData)
        binding.rvListAvailableCarToPick.adapter = listAvailableCarAdapter
    }
}