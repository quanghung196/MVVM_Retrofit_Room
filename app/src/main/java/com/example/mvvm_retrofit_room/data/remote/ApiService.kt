package com.example.mvvm_retrofit_room.data.remote

import com.example.mvvm_retrofit_room.data.remote.RetrofitBuilder.X_API_KEY
import com.example.mvvm_retrofit_room.model.Blog
import retrofit2.http.*

interface ApiService {

    //@Headers("x-api-key: " + RetrofitBuilder.X_API_KEY)
    @GET("/dev/profiles")
    suspend fun getAllBlog(): List<Blog>

    @GET("/dev/profiles/{id}")
    suspend fun getBlogByID(@Path("id") blogID: String): Blog

    @POST("/dev/profiles")
    suspend fun addBlog(@Body blog: Blog): Blog

    @DELETE("/dev/profiles/{id}")
    suspend fun deleteBlog(@Path("id") blogID: String)
}