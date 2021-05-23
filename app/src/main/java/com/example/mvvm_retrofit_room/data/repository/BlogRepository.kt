package com.example.mvvm_retrofit_room.data.repository

import com.example.mvvm_retrofit_room.data.local.BlogDatabase
import com.example.mvvm_retrofit_room.data.local.dao.BlogDAO
import com.example.mvvm_retrofit_room.data.remote.RetrofitBuilder
import com.example.mvvm_retrofit_room.model.Blog

class BlogRepository {

    private val blogDAO: BlogDAO

    init {
        val blogDatabase = BlogDatabase.getInstance()
        blogDAO = blogDatabase.getBlogDAO()
    }

    //remote data
    suspend fun getAllBlogFromServer() = RetrofitBuilder.apiService.getAllBlog()
    suspend fun addBlogToServer(blog: Blog) = RetrofitBuilder.apiService.addBlog(blog = blog)
    suspend fun deteteBlogOnServer(blogID: String) = RetrofitBuilder.apiService.deleteBlog(blogID = blogID)
    suspend fun getBlogFromServerByID(blogID: String) = RetrofitBuilder.apiService.getBlogByID(blogID = blogID)
    suspend fun getBlogUploadableURL() = RetrofitBuilder.apiService.getBlogUploadableURL()

    //local data
    suspend fun synchronizeAllBlogFromServer(blogs: List<Blog>) = blogDAO.synchronizeAllBlogFromServer(blogs = blogs)
    suspend fun deteleAllBlogFromDatabase() = blogDAO.deteleAllBlogFromDatabase()
    fun getAllBlogFromDatabase() = blogDAO.getAllBlogFromDatabase()
}