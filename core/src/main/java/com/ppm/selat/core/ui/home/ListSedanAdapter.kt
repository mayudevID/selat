package com.ppm.selat.core.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.ppm.selat.core.R
import com.ppm.selat.margin
import com.ppm.selat.core.domain.model.Car

class ListSedanAdapter(private var listSedan: ArrayList<Car>) : RecyclerView.Adapter <ListSedanAdapter.ListSedanViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Car)
        fun onItemDeleted(data: Car)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListSedanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val carName:TextView = itemView.findViewById(R.id.sedan_name)
        val price: TextView = itemView.findViewById(R.id.sedan_price_per_day)
        val rating: TextView = itemView.findViewById(R.id.sedan_rating)
        val image: ImageView = itemView.findViewById(R.id.image_sedan)
        val load: ProgressBar = itemView.findViewById(R.id.load_image_sedan)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSedanViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view_sedan, parent, false)
        return ListSedanViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ListSedanViewHolder, position: Int) {
        val data = listSedan[position]
        holder.carName.text = data.carBrand
        holder.price.text = "${data.price/1000.0}K / hari)"
        holder.rating.text = data.rating.toString()
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listSedan[holder.adapterPosition])
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
        return listSedan.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: ArrayList<Car>) {
        listSedan.clear()
        listSedan = newData
        notifyDataSetChanged()
    }
}