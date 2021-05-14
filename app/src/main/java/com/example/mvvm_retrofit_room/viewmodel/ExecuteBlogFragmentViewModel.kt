package com.example.mvvm_retrofit_room.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.mvvm_retrofit_room.data.repository.BlogRepository
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class ExecuteBlogFragmentViewModel : ViewModel() {

    private val mBlogRepository: BlogRepository = BlogRepository()

    //add data lên server
    fun addNewBlogToServer(blog: Blog) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mBlogRepository.addBlogToServer(blog)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    //xóa data trên server
    fun deleteCurrentBlog(blogID: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            mBlogRepository.deteteBlogOnServer(blogID)
            emit(Resource.success(data = mBlogRepository.getBlogFromServerByID(blogID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(ContentValues.TAG, "onCleared: ")
    }
}