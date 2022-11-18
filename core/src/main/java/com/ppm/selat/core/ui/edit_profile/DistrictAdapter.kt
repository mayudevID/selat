package com.ppm.selat.core.ui.edit_profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.District
import com.ppm.selat.core.domain.model.Province
import com.ppm.selat.core.utils.getCapsSentences

class DistrictAdapter(private val context: Context, private var listDistrict: ArrayList<District>) : BaseAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    private class ItemHolder(row: View?) {
        val label: TextView
        init {
            label = row?.findViewById(R.id.text_data_spinner) as TextView
        }
    }

    override fun getCount() = listDistrict.size

    override fun getItem(p0: Int) = listDistrict[p0]

    override fun getItemId(p0: Int) = p0.toLong()

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val newView: View
        val vh: ItemHolder
        if (p1 == null) {
            newView = inflater.inflate(R.layout.custom_region_spinner, null)
            vh = ItemHolder(newView)
            newView?.tag = vh
        } else {
            newView = p1
            vh = newView.tag as ItemHolder
        }

        vh.label.text = getCapsSentences(listDistrict[p0].name.lowercase())
        return newView
    }

    fun refreshData(newList : ArrayList<District>) {
        listDistrict.clear()
        listDistrict = newList
        notifyDataSetChanged()
    }
}