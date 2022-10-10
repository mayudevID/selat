package com.ppm.selat.core.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.R
import com.ppm.selat.margin
import com.ppm.selat.core.domain.model.Car

class ListSuvAdapter(private val listSuv: ArrayList<Car>) : RecyclerView.Adapter <ListSuvAdapter.ListSuvViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Car)
        fun onItemDeleted(data: Car)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListSuvViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val carName: TextView = itemView.findViewById(R.id.suv_name)
        val price: TextView = itemView.findViewById(R.id.suv_price_per_day)
        val rating: TextView = itemView.findViewById(R.id.suv_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSuvViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view_suv, parent, false)
        return ListSuvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListSuvViewHolder, position: Int) {
        val data = listSuv[position]
        holder.carName.text = data.carName
        holder.price.text = "${data.price.toString()}K / hari)"
        holder.rating.text = data.rating.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSuv[holder.adapterPosition])
        }
        holder.itemView.margin(right = 38F)
        if (position == 0) {
            holder.itemView.margin(left = 36F)
        }
    }

    override fun getItemCount(): Int {
        return listSuv.size
    }
}