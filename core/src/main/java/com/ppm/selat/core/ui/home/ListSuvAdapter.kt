package com.ppm.selat.core.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ppm.selat.core.R
import com.ppm.selat.margin
import com.ppm.selat.core.domain.model.Car

class ListSuvAdapter(private var listSuv: ArrayList<Car>) : RecyclerView.Adapter <ListSuvAdapter.ListSuvViewHolder>() {
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
        val image: ImageView = itemView.findViewById(R.id.image_suv)
        val load: ProgressBar = itemView.findViewById(R.id.load_image_suv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSuvViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view_suv, parent, false)
        return ListSuvViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListSuvViewHolder, position: Int) {
        val data = listSuv[position]
        holder.carName.text = data.carBrand
        holder.price.text = "${data.price/1000.0}K / hari)"
        holder.rating.text = data.rating.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSuv[holder.adapterPosition])
        }
        Glide.with(holder.itemView)
            .load(data.carImage.sidePhoto)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.load.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    holder.load.visibility = View.GONE
                    return false
                }

            })
            .into(holder.image)
        holder.itemView.margin(right = 38F)
        if (position == 0) {
            holder.itemView.margin(left = 36F)
        }
    }

    override fun getItemCount(): Int {
        return listSuv.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: ArrayList<Car>) {
        listSuv.clear()
        listSuv = newData
        notifyDataSetChanged()
    }
}