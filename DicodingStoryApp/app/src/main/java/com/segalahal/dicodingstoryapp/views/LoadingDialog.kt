package com.segalahal.dicodingstoryapp.views

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.segalahal.dicodingstoryapp.R

class LoadingDialog(context: Context) {
    private val dialog = Dialog(context)
    fun showDialog(){
        dialog.setContentView(R.layout.dialog_loading)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.create()
        dialog.show()
    }

    fun hideDialog(){
        dialog.dismiss()
    }
}