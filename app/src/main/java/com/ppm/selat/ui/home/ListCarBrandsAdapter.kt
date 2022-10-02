package com.ppm.selat.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.R
import com.ppm.selat.margin
import com.ppm.selat.model.Car

class ListCarBrandsAdapter(private val listSedan: ArrayList<String>) : RecyclerView.Adapter <ListCarBrandsAdapter.ListCarBrandsViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: String)
        fun onItemDeleted(data: String)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListCarBrandsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val brand :TextView = itemView.findViewById(R.id.car_brand_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCarBrandsViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_car_brand, parent, false)
        return ListCarBrandsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListCarBrandsViewHolder, position: Int) {
        val data = listSedan[position]
        holder.brand.text = data
        holder.itemView.margin(right = 5F)
        if (position == 0) {
            holder.itemView.margin(left = 36F)
        }
    }

    override fun getItemCount(): Int {
        return listSedan.size
    }
}