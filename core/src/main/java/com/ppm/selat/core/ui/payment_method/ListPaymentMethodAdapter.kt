package com.ppm.selat.core.ui.payment_method

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.utils.setLogo
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ListPaymentMethodAdapter(private var listDataTypePay: ArrayList<DataTypePay>) : RecyclerView.Adapter<ListPaymentMethodAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    val kursIndonesia: DecimalFormat = DecimalFormat.getCurrencyInstance() as DecimalFormat
    val formatRp = DecimalFormatSymbols()

    init {
        formatRp.currencySymbol = "Rp"
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataTypePay)
        fun onItemDeleted(data: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.logo_target)
        val number: TextView = itemView.findViewById(R.id.number_mp)
        val value: TextView = itemView.findViewById(R.id.balance_mp)
        val deleteMp : CardView = itemView.findViewById(R.id.delete_mp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.payment_method_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listDataTypePay[position]
        holder.image.setImageResource(setLogo(data.name))
        holder.number.text = data.number
        holder.value.text = "Saldo ${kursIndonesia.format(data.value).split(",")[0]}"
        holder.deleteMp.setOnClickListener {
            onItemClickCallback.onItemClicked(listDataTypePay[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listDataTypePay.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newData: ArrayList<DataTypePay>) {
        listDataTypePay.clear()
        listDataTypePay = newData
        notifyDataSetChanged()
    }
}