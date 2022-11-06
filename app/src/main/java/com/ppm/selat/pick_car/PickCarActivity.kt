package com.ppm.selat.pick_car

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.R
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.ui.pick_car.ListAvailableCarAdapter
import com.ppm.selat.core.ui.pick_car.ListCarManufacturerToPickAdapter
import com.ppm.selat.core.utils.*
import com.ppm.selat.databinding.ActivityPickCarBinding
import com.ppm.selat.detail_car.DetailCarActivity
import org.koin.androidx.viewmodel.ext.android.viewModel


class PickCarActivity : AppCompatActivity() {

    private val pickCarViewModel: PickCarViewModel by viewModel()
    private lateinit var binding: ActivityPickCarBinding
    private lateinit var listAvailableCarAdapter: ListAvailableCarAdapter
    private lateinit var listCarManufacturerToPickAdapter: ListCarManufacturerToPickAdapter
    private var carDataList: ArrayList<Car> = ArrayList()

    private var manufacturer: Manufacturer? = null
    private var typeCar: TypeCar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPickCarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        typeCar = intent.getEnumExtra<TypeCar>()
        manufacturer = intent.getEnumExtra<Manufacturer>()

        binding.typeCarRecent.text = convertTypeCarToString(typeCar!!)
        Log.d("PickCarActivity", binding.typeCarRecent.text.toString())

        setAppBarTitle()
        setUpInitialAdapter()
        setUpListener()

        getDataByParams()
    }

    private fun setAppBarTitle() {
        var isShow = true
        var scrollRange = -1
        binding.appBarLayout.addOnOffsetChangedListener { barLayout, verticalOffset ->
            if (scrollRange == -1) {
                scrollRange = barLayout?.totalScrollRange!!
            }

            if (scrollRange + verticalOffset == 0) {
                binding.collapisngToolbar.title = "Mobil yang tersedia"
                isShow = true
            } else if (isShow) {
                binding.collapisngToolbar.title =
                    " "
                isShow = false
            }
        }


    }

    private fun setUpListener() {

        val adapter = CustomTypeCarAdapter(this, arrayListOf("ALL", "SEDAN", "SUV"))

        binding.filterButton.adapter = adapter
        binding.filterButton.setSelection(0, false)
        binding.filterButton.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                typeCar = convertStringToTypeCar(p0?.selectedItem.toString())
                binding.typeCarRecent.text = p0?.selectedItem.toString()
                getDataByParams()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        listCarManufacturerToPickAdapter.setOnItemClickCallback(object :
            ListCarManufacturerToPickAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Int) {
                manufacturer = convertIntToManufacturer(data)
                getDataByParams()
            }

            override fun onItemDeleted(data: Int) {

            }

        })

        listAvailableCarAdapter.setOnItemClickCallback(object :
            ListAvailableCarAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Car) {
                val intent = Intent(this@PickCarActivity, DetailCarActivity::class.java)
                intent.putExtra("CAR_DATA", data)
                startActivity(intent)
            }

            override fun onItemDeleted(data: Car) {

            }
        })

        binding.backButtonFromPick.setOnClickListener {
            finish()
        }
    }

    private fun setUpInitialAdapter() {
        val listImage = arrayListOf(
            R.drawable.all_brands_pick,
            R.drawable.toyota_only_logo,
            R.drawable.honda_only_logo,
            R.drawable.nissan_only_logo,
            R.drawable.hyundai_only_logo,
            R.drawable.suzuki_only_logo,
        )

        listCarManufacturerToPickAdapter = ListCarManufacturerToPickAdapter(
            listImage,
            convertManufacturerToInt(manufacturer!!)
        )
        binding.rvListManufacturerCarToPick.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false,
        )
        binding.rvListManufacturerCarToPick.setHasFixedSize(true)
        binding.rvListManufacturerCarToPick.adapter = listCarManufacturerToPickAdapter

        listAvailableCarAdapter = ListAvailableCarAdapter(carDataList)
        binding.rvListAvailableCarToPick.layoutManager = LinearLayoutManager(this)
        binding.rvListAvailableCarToPick.setHasFixedSize(false)
        binding.rvListAvailableCarToPick.adapter = listAvailableCarAdapter
    }

    private fun getDataByParams() {
        pickCarViewModel.getCarDataByParams(manufacturer!!, typeCar!!)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {
                            binding.nsvDataMissing.visibility = View.GONE
                            binding.nsvDataLoad.visibility = View.GONE
                            binding.nsvDataLoading.visibility = View.VISIBLE
                            Log.d("PickCarActivity", "Loading")
                        }
                        is Resource.Success -> {
                            carDataList.clear()
                            carDataList = ArrayList(result.data!!)
                            if (carDataList.isEmpty() || carDataList.size == 0) {
                                binding.manufacturerTarget.text =
                                    convertManufacturerToString(manufacturer!!)
                                binding.typeTarget.text = convertTypeCarToString(typeCar!!)
                                binding.nsvDataLoading.visibility = View.GONE
                                binding.nsvDataMissing.visibility = View.VISIBLE
                            } else {
                                listAvailableCarAdapter.refreshList(carDataList)
                                binding.nsvDataLoading.visibility = View.GONE
                                binding.nsvDataLoad.visibility = View.VISIBLE
                            }
                            Log.d("PickCarActivity", "Success: ${result.data!!.size}")
                        }
                        is Resource.Error -> {
                            Log.e("PickCarActivity", result.message.toString())
                        }
                    }
                }
            }
    }
}