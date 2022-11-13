package com.ppm.selat.core.ui.pick_car

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.Car
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ListAvailableCarAdapter(private var listAvailableCar: ArrayList<Car>) :
    RecyclerView.Adapter<ListAvailableCarAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    private val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
    private val formatRp = DecimalFormatSymbols()

    init {
        formatRp.currencySymbol = ""
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Car)
        fun onItemDeleted(data: Car)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brand: TextView = itemView.findViewById(R.id.name_car)
        val yearProd: TextView = itemView.findViewById(R.id.id_year)
        val price: TextView = itemView.findViewById(R.id.id_price)
        val imageCar: ImageView = itemView.findViewById(R.id.car_image)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_available_car_cardview, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listAvailableCar[position]
        holder.brand.text = data.carBrand
        Glide.with(holder.itemView)
            .load(data.carImage.primaryPhoto).
            into(holder.imageCar)
        holder.price.text = "${kursIndonesia.format(data.price / 1000).split(",")[0]}K"
        holder.yearProd.text = data.yearProduction.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listAvailableCar[holder.adapterPosition])
        }

    }

    override fun getItemCount(): Int {
        return listAvailableCar.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshList(newList : ArrayList<Car>) {
        listAvailableCar.clear()
        listAvailableCar = newList
        notifyDataSetChanged()
    }
}