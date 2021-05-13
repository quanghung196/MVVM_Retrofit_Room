package com.example.myapplication.utils

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

object ReusableFunctionForEdittext {

    fun clearAllEdittext(group: ViewGroup) {
        var i = 0
        val count = group.childCount
        while (i < count) {
            val view: View = group.getChildAt(i)
            if (view is ViewGroup) {
                clearAllEdittext(view)
            } else if (view is EditText) {
                view.setText("")
            }
            i++
        }
    }

    fun getAllEditText(group: ViewGroup, textList: ArrayList<TextInputEditText>): ArrayList<TextInputEditText> {
        var i = 0
        val count = group.childCount
        while (i < count) {
            val view: View = group.getChildAt(i)
            if (view is ViewGroup) {
                getAllEditText(view, textList)
            } else if (view is TextInputEditText) {
                textList.add(view)
            }
            i++
        }
        Log.i("lala", textList.size.toString())
        return textList
    }

    fun isTextFullfill(textList: ArrayList<TextInputEditText>): Boolean {
        for (textField: TextInputEditText in textList) {
            if (textField.text.toString().isEmpty()) {
                return false
            }
        }
        return true
    }

    fun Context.hideKeyboardInFragment(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


}