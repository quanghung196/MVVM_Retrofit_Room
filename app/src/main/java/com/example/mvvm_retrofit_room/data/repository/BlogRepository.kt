package com.example.mvvm_retrofit_room.data.repository

import com.example.mvvm_retrofit_room.data.local.BlogDatabase
import com.example.mvvm_retrofit_room.data.local.dao.BlogDAO
import com.example.mvvm_retrofit_room.data.remote.BlogApiRetrofitBuilder
import com.example.mvvm_retrofit_room.data.remote.ImageApiRetrofitBuilder
import com.example.mvvm_retrofit_room.model.Blog
import io.reactivex.rxjava3.core.Completable
import okhttp3.MultipartBody
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
    fun getAllBlogFromServer() =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.getAllBlog()

    fun addBlogToServer(blog: Blog) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.addBlog(blog = blog)

    fun deteteBlogOnServer(blogID: String) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.deleteBlog(blogID = blogID)

    fun getBlogUploadableURL(fileName: String) =
        BlogApiRetrofitBuilder.BLOG_API_SERVICE.getBlogUploadableURL(fileName = fileName)

    fun putImageToServer(url: String, imageFile : RequestBody) =
        ImageApiRetrofitBuilder.IMAGE_API_SERVICE.putImageToServer(url = url, imageFile = imageFile)

    //local data
    fun synchronizeAllBlogFromServer(blogs: List<Blog>) =
        blogDAO.synchronizeAllBlogFromServer(blogs = blogs)

    fun deteleAllBlogFromDatabase() =
        blogDAO.deteleAllBlogFromDatabase()

    fun getAllBlogFromDatabase() =
        blogDAO.getAllBlogFromDatabase()
}