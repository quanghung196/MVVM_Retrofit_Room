package com.example.mvvm_retrofit_room.data.remote

import io.reactivex.rxjava3.core.Completable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ImageApiService {

    @PUT
    fun putImageToServer(@Url url: String,
                         @Body imageFile : RequestBody ) : Completable
}