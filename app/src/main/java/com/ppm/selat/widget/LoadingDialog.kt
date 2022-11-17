package com.ppm.selat

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView

fun startLoadingDialog(textDesc: String, activity: Activity) : AlertDialog {
    val dialog: AlertDialog
    val builder = AlertDialog.Builder(activity)
    val inflater = activity.layoutInflater
    val dialogView = inflater.inflate(R.layout.dialog_loading, null)

    builder.setView(dialogView)

    dialog = builder.create()
    dialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
    dialog.window?.setLayout(600, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.setCanceledOnTouchOutside(false)

    val textLoading = dialogView.findViewById<TextView>(R.id.text_desc_cpi)
    textLoading.text = textDesc
    dialog.show()

    return dialog
}