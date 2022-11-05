package com.ppm.selat.core.ui.pick_car

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.core.R
import com.ppm.selat.margin

class ListCarManufacturerToPickAdapter(private val listBrand: ArrayList<Int>, private var rowIndex: Int) :
    RecyclerView.Adapter<ListCarManufacturerToPickAdapter.ListViewHolder>() {
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listBrand[position]
        holder.brand.setImageResource(data)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(position)
            rowIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
        if (holder.adapterPosition == rowIndex) {
            (holder.itemView as CardView).setCardBackgroundColor(Color.parseColor("#FDC500"))
        } else {
            (holder.itemView as CardView).setCardBackgroundColor(Color.WHITE)
        }
        holder.itemView.margin(right = 18F)
        if (holder.adapterPosition == 0) {
            holder.itemView.margin(left = 36F)
        }
    }

    override fun getItemCount(): Int {
        return listBrand.size
    }
}