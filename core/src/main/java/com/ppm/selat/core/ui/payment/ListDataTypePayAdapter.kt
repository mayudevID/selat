package com.ppm.selat.core.ui.payment

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.margin

class ListDataTypePayAdapter(
    private val dataPay: ArrayList<DataTypePay>,
    private var rowIndex: Int
) : RecyclerView.Adapter<ListDataTypePayAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Int)
        fun onItemDeleted(data: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dataValue = itemView.findViewById<TextView>(R.id.value_saldo_terkini)
        val dataNumber = itemView.findViewById<TextView>(R.id.number_card)
        val borderSelected = itemView.findViewById<View>(R.id.border_selected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.single_card, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataPay[position]
        holder.dataNumber.text = data.number
        holder.dataValue.text = data.value
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(position)
            rowIndex = holder.adapterPosition
            notifyDataSetChanged()
        }
        if (holder.adapterPosition == rowIndex) {
            holder.borderSelected.visibility = View.VISIBLE
        } else {
            holder.borderSelected.visibility = View.GONE
        }
        if (position == 0) {
            holder.itemView.margin(left = 36F)
        } else {
            holder.itemView.margin(left = 20.24F)
        }
        if (position == dataPay.size - 1) {
            holder.itemView.margin(right = 36F)
        }
    }

    override fun getItemCount(): Int {
        return dataPay.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun ChangedBetweenCardAndEwallet() {
        rowIndex = -1
        notifyDataSetChanged()
    }
}