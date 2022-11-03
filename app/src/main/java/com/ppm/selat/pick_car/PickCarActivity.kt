package com.ppm.selat.ui.pick_car

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.Manufacturer
import com.ppm.selat.R
import com.ppm.selat.TypeCar
import com.ppm.selat.core.ui.pick_car.ListAvailableCarAdapter
import com.ppm.selat.core.ui.pick_car.ListCarManufacturerToPickAdapter
import com.ppm.selat.databinding.ActivityPickCarBinding
import com.ppm.selat.getEnumExtra
import com.ppm.selat.pick_car.PickCarViewModel


class PickCarActivity : AppCompatActivity() {

    private val pickCarViewModel: PickCarViewModel by viewModels()
    private lateinit var binding: ActivityPickCarBinding
    private lateinit var listAvailableCarAdapter: ListAvailableCarAdapter
    private lateinit var listCarManufacturerToPickAdapter: ListCarManufacturerToPickAdapter

    companion object {
        const val MANUFACTURER: String = "MANUFACTURER"
        const val TYPECAR: String = "TYPE_CAR"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        pickCarViewModel.manufacturer.value = intent.getEnumExtra<Manufacturer>()!!
        pickCarViewModel.typeCar.value = intent.getEnumExtra<TypeCar>()!!

        setAppBarTitle()
        setUpInitialAdapter()
        setUpListener()

        getDataByParams()
    }

    private fun setAppBarTitle() {
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
        listCarManufacturerToPickAdapter.setOnItemClickCallback(object :
            ListCarManufacturerToPickAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Int) {

            }

            override fun onItemDeleted(data: Int) {

            }

        })

//        listAvailableCarAdapter.setOnItemClickCallback(object :
//            ListAvailableCarAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Car) {
//
//            }
//
//            override fun onItemDeleted(data: Car) {
//
//            }
//        })

        binding.backButtonFromPick.setOnClickListener {
            finish()
        }
    }

    private fun setUpInitialAdapter() {
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
            R.drawable.hyundai_only_logo,
            R.drawable.suzuki_only_logo,
        )

        listCarManufacturerToPickAdapter = ListCarManufacturerToPickAdapter(
            listImage,
            convertManufacturerToInt(pickCarViewModel.manufacturer.value)
        )
        binding.rvListBrandsCarToPick.adapter = listCarManufacturerToPickAdapter

        binding.rvListAvailableCarToPick.setHasFixedSize(false)
        binding.rvListAvailableCarToPick.layoutManager = LinearLayoutManager(this)


    }

    private fun getDataByParams() {

        //listAvailableCarAdapter = ListAvailableCarAdapter()
        //binding.rvListAvailableCarToPick.adapter = listAvailableCarAdapter
    }

    private fun convertManufacturerToInt(manufacturer: Manufacturer): Int {
        return when (manufacturer) {
            Manufacturer.TOYOTA -> 1
            Manufacturer.HONDA -> 2
            Manufacturer.NISSAN -> 3
            Manufacturer.HYUNDAI -> 4
            Manufacturer.SUZUKI -> 5
            Manufacturer.ALL -> 0
        }
    }
}