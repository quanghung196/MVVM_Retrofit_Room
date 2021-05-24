package com.example.mvvm_retrofit_room.data.repository

import com.example.mvvm_retrofit_room.data.local.BlogDatabase
import com.example.mvvm_retrofit_room.data.local.dao.BlogDAO
import com.example.mvvm_retrofit_room.data.remote.BlogApiRetrofitBuilder
import com.example.mvvm_retrofit_room.data.remote.ImageApiRetrofitBuilder
import com.example.mvvm_retrofit_room.model.Blog
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Url

class BlogRepository {

    private val blogDAO: BlogDAO

    init {
        val blogDatabase = BlogDatabase.getInstance()
        blogDAO = blogDatabase.getBlogDAO()
    }

    //remote data
    suspend fun getAllBlogFromServer() = BlogApiRetrofitBuilder.BLOG_API_SERVICE.getAllBlog()
    suspend fun addBlogToServer(blog: Blog) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.addBlog(blog = blog)

    suspend fun deteteBlogOnServer(blogID: String) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.deleteBlog(blogID = blogID)

    suspend fun getBlogFromServerByID(blogID: String) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.getBlogByID(blogID = blogID)

    suspend fun getBlogUploadableURL(fileName: String) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.getBlogUploadableURL(fileName = fileName)

    fun putImageToServer(url: String, image: RequestBody) : Call<Void> =
        ImageApiRetrofitBuilder.IMAGE_API_SERVICE.putImageToServer(url = url, image = image)

    //local data
    suspend fun synchronizeAllBlogFromServer(blogs: List<Blog>) =
        blogDAO.synchronizeAllBlogFromServer(blogs = blogs)

    suspend fun deteleAllBlogFromDatabase() = blogDAO.deteleAllBlogFromDatabase()
    fun getAllBlogFromDatabase() = blogDAO.getAllBlogFromDatabase()
}