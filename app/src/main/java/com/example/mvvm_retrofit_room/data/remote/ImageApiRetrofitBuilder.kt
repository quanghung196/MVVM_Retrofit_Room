package com.example.mvvm_retrofit_room.data.remote

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImageApiRetrofitBuilder {

    private const val BASE_URL = "https://profile-bucket-dev.s3.ap-southeast-1.amazonaws.com/"
    //const val HEADER_X_API_KEY = "VRDN7w5XbI7vwlp0cnVYi8xUU6WV3Sma1d9ijGkJ"

    private fun provideClient(): OkHttpClient {
        val headerInterceptor = Interceptor {
            val original: Request = it.request()

            val request: Request = original.newBuilder()
                //.header("x-api-key", HEADER_X_API_KEY)
                .build()

            it.proceed(request)
        }
        val interceptor = HttpLoggingInterceptor()
        /*interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)*/
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(headerInterceptor)
            .build()
    }

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideClient())
        .addConverterFactory(GsonConverterFactory.create())

    val retrofit = builder.build()
    val IMAGE_API_SERVICE: ImageApiService = retrofit.create(ImageApiService::class.java)
}