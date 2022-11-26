package com.ppm.selat.transaction

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.ppm.selat.core.data.Resource
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.ui.transaction.ListTransactionAdapter
import com.ppm.selat.core.utils.isNetworkAvailable
import com.ppm.selat.databinding.ActivityTransactionBinding
import com.ppm.selat.payment.PaymentActivity
import com.ppm.selat.proof_booking.ProofBookingActivity
import com.ppm.selat.startLoadingDialog
import com.ppm.selat.widget.onSnackError
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionActivity : AppCompatActivity() {

    private val transactionViewModel: TransactionViewModel by viewModel()
    private lateinit var binding: ActivityTransactionBinding
    private lateinit var listTransactionAdapter: ListTransactionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.errorMessageLoadTransaction.visibility = View.GONE
        binding.rvTransaction.visibility = View.GONE
        binding.emptyTransaction.visibility = View.GONE

        binding.rvTransaction.layoutManager = LinearLayoutManager(this)
        binding.rvTransaction.setHasFixedSize(true)

        setUpListener()
        loadData()
    }

    private fun setUpListener() {
        binding.backButtonTransaction.setOnClickListener {
            finish()
        }

        binding.retryTransactionButton.setOnClickListener {
            loadData()
        }
    }

    private fun loadData() {
        transactionViewModel.getHistoryOrder().observe(this) {
            result ->
            if (result != null) {
                when (result) {
                    is Resource.Loading -> {
                        binding.errorMessageLoadTransaction.visibility = View.GONE
                        binding.rvTransaction.visibility = View.GONE
                        binding.emptyTransaction.visibility = View.GONE
                        binding.loadTransaction.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        val listData = result.data!!
                        if (listData.isEmpty()) {
                            binding.loadTransaction.visibility = View.GONE
                            binding.emptyTransaction.visibility = View.VISIBLE
                        } else {
                            Log.d("TransactionActivity", listData.toString())
                            listTransactionAdapter = ListTransactionAdapter(ArrayList(listData))
                            binding.rvTransaction.adapter = listTransactionAdapter
                            binding.loadTransaction.visibility = View.GONE
                            binding.rvTransaction.visibility = View.VISIBLE

                            listTransactionAdapter.setOnItemClickCallback(object : ListTransactionAdapter.OnItemClickCallback {
                                override fun onItemCheckedClicked(data: OrderData) {
                                    val intent = Intent(this@TransactionActivity, ProofBookingActivity::class.java)
                                    intent.putExtra("ORDER_DATA", data)
                                    startActivity(intent)
                                }

                                override fun onItemRentClicked(data: OrderData) {
                                    getDataToRentAgain(data.idCar)
                                }
                            })
                        }
                    }
                    is Resource.Error -> {
                        binding.rvTransaction.visibility = View.GONE
                        binding.emptyTransaction.visibility = View.GONE
                        binding.loadTransaction.visibility = View.GONE
                        binding.errorMessageLoadTransaction.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getDataToRentAgain(idCar: String) {
        if (isNetworkAvailable(this@TransactionActivity)) {
            val dialog = startLoadingDialog("Sedang mengambil data mobil...", this@TransactionActivity)
            transactionViewModel.getSingleCarData(idCar).observe(this) {
                    result ->
                if (result != null) {
                    when (result) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            dialog.dismiss()
                            finish()
                            val intent = Intent(this@TransactionActivity, PaymentActivity::class.java)
                            intent.putExtra("CAR_DATA", result.data!!)
                            startActivity(intent)
                        }
                        is Resource.Error -> {
                            dialog.dismiss()
                            onSnackError(result.message.toString(), binding.root, this@TransactionActivity)
                        }
                    }
                }
            }
        } else {
            onSnackError("Tidak dapat terhubung ke internet", binding.root, this@TransactionActivity)
        }
    }
}