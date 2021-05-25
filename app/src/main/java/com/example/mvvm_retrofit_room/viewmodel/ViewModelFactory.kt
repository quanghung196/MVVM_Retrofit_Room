package com.example.mvvm_retrofit_room.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogListFragmentViewModel::class.java)) {
            return BlogListFragmentViewModel(context = context) as T
        } else if (modelClass.isAssignableFrom(ExecuteBlogFragmentViewModel::class.java)) {
            return ExecuteBlogFragmentViewModel(context = context) as T
        }
        throw IllegalAccessException("Unable construct viewModel")
    }
}