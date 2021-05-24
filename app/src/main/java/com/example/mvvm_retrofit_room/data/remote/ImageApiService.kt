package com.example.mvvm_retrofit_room.data.remote

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url


interface ImageApiService {

    @PUT
    fun putImageToServer(@Url url: String, @Body image: RequestBody) : Call<Void>
}