package com.example.mvvm_retrofit_room.view.customview

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.databinding.CustomProgressDialogBinding


class CustomProgressDialog(val activity: Activity) : Dialog(activity) {

    private var mBinding: CustomProgressDialogBinding

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.custom_progress_dialog, null, false)
        setContentView(mBinding.root)

        val window: Window? = getWindow()
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    fun setTitle(message: String){
        mBinding.tvTitle.text = message
    }
}