package com.ppm.selat.widget

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.ppm.selat.R

fun onSnackError(errorMessage: String, view: View, context: Context) {
    val snackbar = Snackbar.make(
        view, convertCode(errorMessage),
        Snackbar.LENGTH_LONG
    ).setAction("Action", null)
    val snackbarView = snackbar.view

    val textView =
        snackbarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
    textView.setTextColor(Color.WHITE)
    val typeface = ResourcesCompat.getFont(context, R.font.montserrat_medium)
    textView.typeface = typeface
    textView.textSize = 12f
    snackbar.show()
}

fun convertCode(errorCode: String): String {
    return when (errorCode) {
        "ERROR_WRONG_PASSWORD", "ERROR_USER_NOT_FOUND" -> {
            "Email atau password salah"
        }
        "ERROR_INVALID_EMAIL" -> {
            "Email tidak valid"
        }
        "ERROR_REQUIRES_RECENT_LOGIN" -> {
            "(Autentikasi Diperlukan) Harap logout akun dan login kembali untuk mengubah email lama"
        }
        else -> {
            errorCode
        }
    }
}