package com.example.mvvm_retrofit_room.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import com.example.mvvm_retrofit_room.data.remote.ApiService
import com.example.mvvm_retrofit_room.data.remote.RetrofitBuilder.HEADER_X_API_KEY
import com.example.mvvm_retrofit_room.data.repository.BlogRepository
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.Resource
import com.example.mvvm_retrofit_room.view.listener.BlogListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.lang.Exception


class BlogListFragmentViewModel() : ViewModel() {

    private val mBlogRepository: BlogRepository = BlogRepository()
    private lateinit var blogListener: BlogListener

    //lấy data từ api
    fun getAllBlogFromServer() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mBlogRepository.getAllBlogFromServer()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    //đồng bộ data từ api vào room
    fun synchronizeAllBlogFromServer(blogs: List<Blog>) = viewModelScope.launch {
        mBlogRepository.synchronizeAllBlogFromServer(blogs)
    }

    //xóa hết data trong room
    fun deteleAllBlogFromDatabase() = viewModelScope.launch {
        mBlogRepository.deteleAllBlogFromDatabase()
    }

    //lấy data từ room
    fun getAllBlogFromDatabase(): LiveData<List<Blog>> = mBlogRepository.getAllBlogFromDatabase()

    fun setBlogListener(blogListener: BlogListener) {
        this.blogListener = blogListener
    }

    fun onUserClicked(blog: Blog) {
        blogListener.onBlogClicked(blog)
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "onCleared: ")
    }
}