package com.ppm.selat.core.ui.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ppm.selat.core.R
import com.ppm.selat.core.domain.model.DataTypePay
import com.ppm.selat.core.utils.setLogoEWallet
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ListEWalletAdapter(private val listEWallet: ArrayList<DataTypePay>) :
    RecyclerView.Adapter<ListEWalletAdapter.ViewHolder>() {
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

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo: ImageView = itemView.findViewById(R.id.logo_list)
        val balance: TextView = itemView.findViewById(R.id.balance_list)
        val dropDownDart: ImageView = itemView.findViewById(R.id.expand_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_ewallet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataTypePay = listEWallet[position]
        holder.logo.setImageResource(setLogoEWallet(dataTypePay.name))
        holder.balance.text = "Saldo ${kursIndonesia.format(dataTypePay.value).split(",")[0]}"
        holder.dropDownDart.visibility = View.GONE
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listEWallet[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return listEWallet.size
    }
}