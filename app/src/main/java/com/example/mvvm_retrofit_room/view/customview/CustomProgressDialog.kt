package com.example.mvvm_retrofit_room.view.customview

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.TextView
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.view.fragment.BlogExecuteFragment

object CustomProgressDialog {

    fun showDialog(message: String, context: Activity) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_progress_dialog)

        val title = dialog.findViewById(R.id.tvTitle) as TextView
        title.text = message
    }

}