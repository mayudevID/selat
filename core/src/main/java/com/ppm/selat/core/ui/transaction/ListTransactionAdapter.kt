package com.ppm.selat.core.ui.transaction

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firestore.v1.StructuredQuery.Order
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.Car
import com.ppm.selat.core.domain.model.OrderData
import com.ppm.selat.core.ui.pick_car.ListAvailableCarAdapter
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ListTransactionAdapter(
    private val listTransaction: ArrayList<OrderData>
) : RecyclerView.Adapter<ListTransactionAdapter.ViewHolder>() {
    val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRp = DecimalFormatSymbols()
    private lateinit var onItemClickCallback: OnItemClickCallback

    init {
        formatRp.currencySymbol = ""
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp
    }

    interface OnItemClickCallback {
        fun onItemCheckedClicked(data: OrderData)
        fun onItemRentClicked(data: OrderData)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderDate: TextView = itemView.findViewById(R.id.tgl_order)
        val brandName: TextView = itemView.findViewById(R.id.brand_order)
        val orderNum: TextView = itemView.findViewById(R.id.order_number)
        val priceTotal: TextView = itemView.findViewById(R.id.harga_total)
        val dayPriceCount: TextView = itemView.findViewById(R.id.rincian_mobil)

        val buttonCheck: CardView = itemView.findViewById(R.id.check_button)
        val buttonRentAgain: CardView = itemView.findViewById(R.id.rent_again_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.transaction_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderData = listTransaction[position]
        val price = kursIndonesia.format(orderData.price * orderData.rentDays / 1000).split(",")[0]
        holder.orderDate.text = orderData.dateOrder
        holder.brandName.text = orderData.brand
        holder.orderNum.text = orderData.id
        holder.priceTotal.text = price
        holder.dayPriceCount.text =
            "${kursIndonesia.format(orderData.price)} / Hari x ${orderData.rentDays} Hari"

        holder.buttonCheck.setOnClickListener {
            onItemClickCallback.onItemCheckedClicked(listTransaction[holder.adapterPosition])
        }

        holder.buttonRentAgain.setOnClickListener {
            onItemClickCallback.onItemRentClicked(listTransaction[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listTransaction.size
    }
}