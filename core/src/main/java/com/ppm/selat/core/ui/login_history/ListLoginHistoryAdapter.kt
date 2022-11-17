package com.ppm.selat.core.ui.login_history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.core.R

class ListLoginHistoryAdapter(private val listLoginHistory: ArrayList<List<String>>) :
    RecyclerView.Adapter<ListLoginHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tglLogin: TextView = itemView.findViewById(R.id.tgl_login)
        val brandPhone: TextView = itemView.findViewById(R.id.brand_phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.login_history_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loginData = listLoginHistory[position]
        holder.tglLogin.text = loginData[0]
        holder.brandPhone.text = loginData[1]
    }

    override fun getItemCount(): Int {
        return listLoginHistory.size
    }
}