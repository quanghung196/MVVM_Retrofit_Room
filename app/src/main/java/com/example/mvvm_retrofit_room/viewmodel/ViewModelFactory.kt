package com.example.mvvm_retrofit_room.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogListFragmentViewModel::class.java)) {
            return BlogListFragmentViewModel() as T
        } else if (modelClass.isAssignableFrom(ExecuteBlogFragmentViewModel::class.java)) {
            return ExecuteBlogFragmentViewModel() as T
        }
        throw IllegalAccessException("Unable construct viewModel")
    }
}