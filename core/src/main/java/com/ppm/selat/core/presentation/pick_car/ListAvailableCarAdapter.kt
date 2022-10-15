package com.ppm.selat.core.presentation.pick_car

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.Car

class ListAvailableCarAdapter(private val listAvailableCar: ArrayList<Car>) :
    RecyclerView.Adapter<ListAvailableCarAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

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
        holder.brand.text = data.carName
        holder.imageCar.setImageResource(R.drawable.temp_car_fortuner)
        holder.price.text = data.price.toString()
        holder.yearProd.text = data.yearProduction.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listAvailableCar[holder.adapterPosition])
        }
//        if (position == 0) {
//            holder.itemView.margin(top = 26F)
//        }
    }

    override fun getItemCount(): Int {
        return listAvailableCar.size
    }
}