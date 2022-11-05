package com.ppm.selat.location_car

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ppm.selat.R
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.databinding.ActivityLocationCarBinding

class LocationCarActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityLocationCarBinding
    private lateinit var carData: Car

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carData = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("CAR_DATA", Car::class.java)!!
        } else {
            intent.getParcelableExtra("CAR_DATA")!!
        }

        supportActionBar?.hide()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setUpListener()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        //  Add a marker in Sydney and move the camera
        //  val sydney = LatLng(-34.0, 151.0)
        //  mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //  mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val locationCar = LatLng(carData.latLng[0], carData.latLng[1])

        val poiMarker = mMap.addMarker(
            MarkerOptions()
                .position(locationCar)
                .title("${carData.carManufacturer} - ${carData.carBrand}")
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationCar, 15f))

        poiMarker?.showInfoWindow()
//        mMap.setOnPoiClickListener { pointOfInterest ->
//            val poiMarker = mMap.addMarker(
//                MarkerOptions()
//                    .position(pointOfInterest.latLng)
//                    .title(pointOfInterest.name)
//                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//            )
//            poiMarker?.showInfoWindow()
//        }
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            finish()
        }
    }
}