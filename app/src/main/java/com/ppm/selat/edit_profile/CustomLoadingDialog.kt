package com.ppm.selat.edit_profile

import android.app.Activity
import android.app.AlertDialog
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import com.ppm.selat.R

class CustomLoadingDialog constructor(activity: Activity) {
    private val currentActivity: Activity = activity

    fun startLoadingDialog(textDesc: String) : AlertDialog{
        val dialog: AlertDialog
        val builder = AlertDialog.Builder(currentActivity)
        val inflater = currentActivity.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_loading, null)

        builder.setView(dialogView)

        dialog = builder.create()
        dialog.window?.decorView?.setBackgroundResource(R.drawable.bg_dialog_border)
        dialog.window?.setLayout(600, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        val textLoading = dialogView.findViewById<TextView>(R.id.text_desc_cpi)
        textLoading.text = textDesc
        return dialog
    }
}