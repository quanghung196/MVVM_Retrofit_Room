package com.example.mvvm_retrofit_room.data.remote

import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.model.BlogImageUploadURL
import retrofit2.http.*

interface BlogApiService {

    //@Headers("x-api-key: " + RetrofitBuilder.HEADER_X_API_KEY)
    @GET("/dev/profiles")
    suspend fun getAllBlog(): List<Blog>

    @GET("/dev/profiles/{id}")
    suspend fun getBlogByID(@Path("id") blogID: String): Blog

    @GET("/dev/profiles/upload-url")
    suspend fun getBlogUploadableURL(@Query("name") fileName : String): BlogImageUploadURL

    @POST("/dev/profiles")
    suspend fun addBlog(@Body blog: Blog): Blog

    @DELETE("/dev/profiles/{id}")
    suspend fun deleteBlog(@Path("id") blogID: String)
}