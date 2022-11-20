package com.ppm.selat.core.utils

import com.ppm.selat.core.R

fun setLogo(name: String): Int {
    return when (name) {
        "GOPAY" -> {
            com.ppm.selat.core.R.drawable.gopay_logo
        }
        "MASTERCARD" -> {
            com.ppm.selat.core.R.drawable.mastercard_logo
        }
        "DANA" -> {
            com.ppm.selat.core.R.drawable.dana_logo
        }
        "OVO" -> {
            com.ppm.selat.core.R.drawable.ovo_logo
        }
        "PAYPAL" -> {
            com.ppm.selat.core.R.drawable.paypal_logo
        }
        else -> {
            com.ppm.selat.core.R.drawable.fluent_payment_20_filled
        }
    }
}