package com.ppm.selat.core.utils

import com.ppm.selat.core.R

fun setLogoEWallet(name: String): Int {
    return when (name) {
        "GOPAY" -> {
            R.drawable.gopay_logo
        }
        else -> {
            R.drawable.mastercard_logo
        }
    }
}