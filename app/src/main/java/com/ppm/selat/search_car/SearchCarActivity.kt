package com.ppm.selat.search_car

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.ui.pick_car.ListAvailableCarAdapter
import com.ppm.selat.databinding.ActivitySearchCarBinding
import com.ppm.selat.detail_car.DetailCarActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList

class SearchCarActivity : AppCompatActivity() {

    private val searchCarViewModel: SearchCarViewModel by viewModel()
    private lateinit var binding: ActivitySearchCarBinding
    private lateinit var listAvailableCarAdapter: ListAvailableCarAdapter
    private var carDataList: ArrayList<Car> = ArrayList()
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchCarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setEmpty()
        setUpAdapter()
        setUpListener()
        initFocus()

        //BackSystemPressed
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backFromPage()
            }
        })
    }

    private fun initFocus() {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        Handler(Looper.getMainLooper()).postDelayed({
            binding.searchBarT.requestFocus();
            imm.showSoftInput(binding.searchBarT, 0);
        }, 100)
    }

    private fun setUpAdapter() {
        listAvailableCarAdapter = ListAvailableCarAdapter(carDataList)
        binding.rvListAvailableCarSearch.layoutManager = LinearLayoutManager(this)
        binding.rvListAvailableCarSearch.setHasFixedSize(false)
        binding.rvListAvailableCarSearch.adapter = listAvailableCarAdapter
    }

    private fun setUpListener() {
        binding.backButton.setOnClickListener {
            backFromPage()
        }

        listAvailableCarAdapter.setOnItemClickCallback(object :
            ListAvailableCarAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Car) {
                val intent = Intent(this@SearchCarActivity, DetailCarActivity::class.java)
                intent.putExtra("CAR_DATA", data)
                startActivity(intent)
            }

            override fun onItemDeleted(data: Car) {

            }
        })

        binding.searchBarT.doOnTextChanged { text, _, _, _ ->
            val valTemp = text.toString().trim()
            if (valTemp.isEmpty() || valTemp == "") {
                searchJob?.cancel()
                carDataList.clear()
                setEmpty()
            } else {
                searchJob?.cancel()
                searchJob = lifecycleScope.launch {
                    delay(300)
                    getData(valTemp)
                }
            }
        }
    }

    private fun setEmpty() {
        binding.rvListAvailableCarSearch.visibility = View.GONE
        binding.dataSearchLoading.visibility = View.GONE
        binding.dataSearchInit.visibility = View.VISIBLE
    }

    private fun getData(valTemp: String) {
        searchCarViewModel.getDataBySearch(capitalize(valTemp)!!).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        binding.dataSearchInit.visibility = View.GONE
                        binding.rvListAvailableCarSearch.visibility = View.GONE
                        binding.dataSearchLoading.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        carDataList.clear()
                        carDataList = ArrayList(result.data!!)
                        listAvailableCarAdapter.refreshList(carDataList)
                        binding.dataSearchLoading.visibility = View.GONE
                        binding.dataSearchInit.visibility = View.GONE
                        binding.rvListAvailableCarSearch.visibility = View.VISIBLE
                    }
                    is Resource.Error -> {

                    }
                }
            }
        }
    }

    private fun backFromPage() {
        window.setWindowAnimations(0);
        finish()
    }

    private fun capitalize(str: String): String? {
        return str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }
}