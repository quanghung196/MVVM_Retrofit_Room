package com.example.mvvm_retrofit_room.data.repository

import com.example.mvvm_retrofit_room.data.remote.RetrofitBuilder
import com.example.mvvm_retrofit_room.model.Blog

class BlogRepository {

    init {

    }

    //remote data
    suspend fun getAllBlogFromServer() = RetrofitBuilder.apiService.getAllBlog()
    suspend fun addBlogToServer(blog: Blog) = RetrofitBuilder.apiService.addBlog(blog)
    suspend fun deteteBlogOnServer(blogID: String) = RetrofitBuilder.apiService.deleteBlog(blogID = blogID)
    suspend fun getBlogFromServerByID(blogID: String) = RetrofitBuilder.apiService.getBlogByID(blogID = blogID)
}