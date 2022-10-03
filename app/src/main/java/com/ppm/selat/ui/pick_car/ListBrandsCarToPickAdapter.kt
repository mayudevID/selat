package com.ppm.selat.ui.pick_car

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.R
import com.ppm.selat.margin

class ListBrandsCarToPickAdapter(private val listBrand: ArrayList<Int>) :
    RecyclerView.Adapter<ListBrandsCarToPickAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Int)
        fun onItemDeleted(data: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brand: ImageView = itemView.findViewById(R.id.logo_base)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_car_brands_to_pick_cardview, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listBrand[position]
        holder.brand.setImageResource(data)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listBrand[holder.adapterPosition])
        }
        holder.itemView.margin(right = 18F)
        if (position == 0) {
            holder.itemView.margin(left = 36F)
        }
    }

    override fun getItemCount(): Int {
        return listBrand.size
    }
}