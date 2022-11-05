package com.ppm.selat.pick_car

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ppm.selat.R

class CustomTypeCarAdapter(private val context: Context, private val listTypeCar: List<String>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private class ItemHolder(row: View?) {
        val label: TextView
        init {
            label = row?.findViewById(R.id.text_item_spinner) as TextView
        }
    }

    override fun getCount() = listTypeCar.size

    override fun getItem(p0: Int) = listTypeCar[p0]

    override fun getItemId(p0: Int) = p0.toLong()

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val newView: View
        val vh: ItemHolder
        if (p1 == null) {
            newView = inflater.inflate(R.layout.custom_type_car_spinner, null)
            vh = ItemHolder(newView)
            newView?.tag = vh
        } else {
            newView = p1
            vh = newView.tag as ItemHolder
        }

        vh.label.text = listTypeCar[p0]
        return newView
    }


}