package com.example.mvvm_retrofit_room.data.remote

import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.model.BlogImageUploadURL
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.*

interface BlogApiService {

    //@Headers("x-api-key: " + RetrofitBuilder.HEADER_X_API_KEY)
    @GET("/dev/profiles")
    fun getAllBlog(): Observable<List<Blog>>

    @GET("/dev/profiles/{id}")
    fun getBlogByID(@Path("id") blogID: String): Maybe<Blog>

    @GET("/dev/profiles/upload-url")
    fun getBlogUploadableURL(@Query("name") fileName: String): Maybe<BlogImageUploadURL>

    @POST("/dev/profiles")
    fun addBlog(@Body blog: Blog): Completable

    @DELETE("/dev/profiles/{id}")
    fun deleteBlog(@Path("id") blogID: String): Completable
}