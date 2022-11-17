package com.ppm.selat.login_history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.ui.login_history.ListLoginHistoryAdapter
import com.ppm.selat.databinding.ActivityLoginHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginHistoryActivity : AppCompatActivity() {

    private val loginHistoryViewModel : LoginHistoryViewModel by viewModel()
    private lateinit var binding: ActivityLoginHistoryBinding
    private lateinit var listLoginHistoryAdapter: ListLoginHistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.errorMessageLoadLoginHistory.visibility = View.GONE
        binding.rvLoginHistory.visibility = View.GONE
        binding.emptyLoginHistory.visibility = View.GONE

        binding.rvLoginHistory.layoutManager = LinearLayoutManager(this)
        binding.rvLoginHistory.setHasFixedSize(true)

        setUpListener()
        loadData()
    }

    private fun setUpListener() {
        binding.backButtonLoginHistory.setOnClickListener {
            finish()
        }

        binding.retryLoginHistoryButton.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        loginHistoryViewModel.getHistoryLogin().observe(this) {
                result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        binding.errorMessageLoadLoginHistory.visibility = View.GONE
                        binding.rvLoginHistory.visibility = View.GONE
                        binding.emptyLoginHistory.visibility = View.GONE
                        binding.loadLoginHistory.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        val listData = result.data!!
                        if (listData.isEmpty()) {
                            binding.emptyLoginHistory.visibility = View.VISIBLE
                        } else {
                            Log.d("LoginHistoryActivity", listData.toString())
                            listLoginHistoryAdapter = ListLoginHistoryAdapter(ArrayList(listData))
                            binding.rvLoginHistory.adapter = listLoginHistoryAdapter
                            binding.loadLoginHistory.visibility = View.GONE
                            binding.rvLoginHistory.visibility = View.VISIBLE
                        }
                    }
                    is Resource.Error -> {
                        binding.rvLoginHistory.visibility = View.GONE
                        binding.emptyLoginHistory.visibility = View.GONE
                        binding.loadLoginHistory.visibility = View.GONE
                        binding.errorMessageLoadLoginHistory.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}