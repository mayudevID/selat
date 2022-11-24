package com.ppm.selat.edit_profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.District
import com.ppm.selat.core.domain.model.Province
import com.ppm.selat.core.domain.model.Regency
import com.ppm.selat.core.domain.model.Village
import com.ppm.selat.core.ui.edit_profile.DistrictAdapter
import com.ppm.selat.core.ui.edit_profile.ProvinceAdapter
import com.ppm.selat.core.ui.edit_profile.RegencyAdapter
import com.ppm.selat.core.ui.edit_profile.VillageAdapter
import com.ppm.selat.core.utils.TypeDataEdit
import com.ppm.selat.core.utils.convertStringToTypeCar
import com.ppm.selat.core.utils.getCapsSentences
import com.ppm.selat.core.utils.isNetworkAvailable
import com.ppm.selat.databinding.ActivityEditAddressBinding
import com.ppm.selat.pick_car.CustomTypeCarAdapter
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.widget.onSnackError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAddressBinding
    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private lateinit var provinceAdapter: ProvinceAdapter
    private lateinit var regencyAdapter: RegencyAdapter
    private lateinit var districtAdapter: DistrictAdapter
    private lateinit var villageAdapter: VillageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.errorText.alpha = 0F
        binding.saveButtonAddress.isClickable = false
        binding.errorText.text = "Mohon isi tempat lebih dari 10 (sepuluh) karakter"
        binding.saveButtonAddress.visibility = View.GONE

        editProfileViewModel.editMode = TypeDataEdit.ADDRESS

        provinceAdapter = ProvinceAdapter(applicationContext, arrayListOf())
        binding.spinnerProvinsi.adapter = provinceAdapter

        regencyAdapter = RegencyAdapter(applicationContext, arrayListOf())
        binding.spinnerKabkota.adapter = regencyAdapter

        districtAdapter = DistrictAdapter(applicationContext,  arrayListOf())
        binding.spinnerKecamatan.adapter = districtAdapter

        villageAdapter = VillageAdapter(applicationContext, arrayListOf())
        binding.spinnerKelurahan.adapter = villageAdapter

        setUpListener()
        setUpAdapterProvince()
    }

    private fun setUpListener() {
        binding.backButtonEdit.setOnClickListener {
            finish()
        }

        binding.editTextBase.doOnTextChanged { text, start, before, count ->
            val value = text.toString().trim()

            if (value == "" || value.isEmpty() || value.length < 10) {
                binding.errorText.alpha = 1F
                binding.saveButtonAddress.isClickable = false
            } else {
                binding.saveButtonAddress.visibility = View.VISIBLE
                binding.errorText.alpha = 0F
                editProfileViewModel.textValue = value
                binding.saveButtonAddress.isClickable = true
            }
        }

        lifecycleScope.launch {
            editProfileViewModel.provinceTarget?.collect {
                binding.targetProvinsi.text = if (it.name == "") "" else getCapsSentences(it.name.lowercase())
            }
        }

        lifecycleScope.launch {
            editProfileViewModel.regencyTarget?.collect {
                binding.targetKabkota.text = if (it.name == "") "" else getCapsSentences(it.name.lowercase())
            }
        }

        lifecycleScope.launch {
            editProfileViewModel.districtTarget?.collect {
                binding.targetKecamatan.text = if (it.name == "") "" else getCapsSentences(it.name.lowercase())
            }
        }

        lifecycleScope.launch {
            editProfileViewModel.villageTarget?.collect {
                binding.targetKelurahan.text = if (it.name == "") "" else getCapsSentences(it.name.lowercase())
            }
        }

        binding.saveButtonAddress.setOnClickListener {
            if (editProfileViewModel.provinceTarget.value.name == "") {
                onSnackError("Pilih provinsi terlebih dahulu", binding.root, this@EditAddressActivity)
            } else if (editProfileViewModel.regencyTarget.value.name == "") {
                onSnackError("Pilih kabupaten/kota terlebih dahulu", binding.root, this@EditAddressActivity)
            } else if (editProfileViewModel.districtTarget.value.name == "") {
                onSnackError("Pilih kecamatan terlebih dahulu", binding.root, this@EditAddressActivity)
            } else if (editProfileViewModel.villageTarget.value.name == "") {
                onSnackError("Pilih desa terlebih dahulu", binding.root, this@EditAddressActivity)
            } else {
                val dialog = startLoadingDialog("Simpan data...", this@EditAddressActivity)
                editProfileViewModel.updateAddress().observe(this) {
                    result -> if (result != null) {
                        when (result) {
                            is Resource.Loading -> {

                            }
                            is Resource.Success -> {
                                dialog.dismiss()
                                finish()
                                Toast.makeText(applicationContext, "Sukses disimpan", Toast.LENGTH_LONG).show()
                            }
                            is Resource.Error -> {
                                dialog.dismiss()
                                Toast.makeText(applicationContext, result.message, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpAdapterProvince() {
        fun applyListProvince(listData: List<Province>) {
            binding.spinnerProvinsi.setSelection(0, false)
            provinceAdapter.refreshData(ArrayList(listData))
            binding.spinnerProvinsi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setUpAdapterRegency(p0?.selectedItem as Province)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        if (isNetworkAvailable(this@EditAddressActivity)) {
            val loading = startLoadingDialog("Loading...", this@EditAddressActivity)
            editProfileViewModel.getProvince().observe(this) {
                result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            loading.dismiss()
                            applyListProvince(result.data!!)
                        }
                        is Resource.Error -> {
                            loading.dismiss()
                            finish()
                            Toast.makeText(this@EditAddressActivity, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, this@EditAddressActivity)
        }
    }

    private fun setUpAdapterRegency(province : Province) {
        fun applyListRegency(listData: List<Regency>) {
            binding.spinnerKabkota.setSelection(0, false)
            regencyAdapter.refreshData(ArrayList(listData))
            binding.spinnerKabkota.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setUpAdapterDistrict(p0?.selectedItem as Regency)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        if (isNetworkAvailable(this@EditAddressActivity)) {
            val loading = startLoadingDialog("Loading...", this@EditAddressActivity)
            editProfileViewModel.getRegency(province.id).observe(this) {
                    result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            loading.dismiss()
                            editProfileViewModel.provinceTarget?.value = province
                            editProfileViewModel.regencyTarget?.value = Regency("", "","")
                            regencyAdapter.refreshData(arrayListOf())
                            editProfileViewModel.districtTarget?.value = District("","","")
                            districtAdapter.refreshData(arrayListOf())
                            editProfileViewModel.villageTarget?.value = Village("","","")
                            villageAdapter.refreshData(arrayListOf())
                            applyListRegency(result.data!!)
                        }
                        is Resource.Error -> {
                            loading.dismiss()
                            finish()
                            Toast.makeText(this@EditAddressActivity, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, this@EditAddressActivity)
        }
    }

    private fun setUpAdapterDistrict(regency: Regency) {
        fun applyListDistrict(listData: List<District>) {
            binding.spinnerKecamatan.setSelection(0, false)
            districtAdapter.refreshData(ArrayList(listData))
            binding.spinnerKecamatan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    setUpAdapterVillage(p0?.selectedItem as District)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        if (isNetworkAvailable(this@EditAddressActivity)) {
            val loading = startLoadingDialog("Loading...", this@EditAddressActivity)
            editProfileViewModel.getDistrict(regency.id).observe(this) {
                    result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            loading.dismiss()
                            editProfileViewModel.regencyTarget?.value = regency
                            editProfileViewModel.districtTarget?.value = District("","","")
                            districtAdapter.refreshData(arrayListOf())
                            editProfileViewModel.villageTarget?.value = Village("","","")
                            villageAdapter.refreshData(arrayListOf())
                            applyListDistrict(result.data!!)
                        }
                        is Resource.Error -> {
                            loading.dismiss()
                            finish()
                            Toast.makeText(this@EditAddressActivity, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, this@EditAddressActivity)
        }
    }

    private fun setUpAdapterVillage(district: District) {
        fun applyListVillage(listData: List<Village>) {
            binding.spinnerKelurahan.setSelection(0, false)
            villageAdapter.refreshData(ArrayList(listData))
            binding.spinnerKelurahan.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    editProfileViewModel.villageTarget?.value = p0?.selectedItem as Village
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }

        if (isNetworkAvailable(this@EditAddressActivity)) {
            val loading = startLoadingDialog("Loading...", this@EditAddressActivity)
            editProfileViewModel.getVillage(district.id).observe(this) {
                    result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {

                        }
                        is Resource.Success -> {
                            loading.dismiss()
                            editProfileViewModel.districtTarget?.value = district
                            editProfileViewModel.villageTarget?.value = Village("","", "")
                            applyListVillage(result.data!!)
                        }
                        is Resource.Error -> {
                            loading.dismiss()
                            finish()
                            Toast.makeText(this@EditAddressActivity, "Tidak dapat terhubung ke internet", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, this@EditAddressActivity)
        }
    }
}