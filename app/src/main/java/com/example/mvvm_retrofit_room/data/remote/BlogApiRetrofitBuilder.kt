package com.example.mvvm_retrofit_room.data.remote

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/*
* Class BlogApiRetrofitBuilder dùng để config những dữ liệu liên quan đến server
* */
object BlogApiRetrofitBuilder {

    private const val BASE_URL = "https://ue3n9ksue9.execute-api.ap-southeast-1.amazonaws.com/"
    private const val HEADER_X_API_KEY = "VRDN7w5XbI7vwlp0cnVYi8xUU6WV3Sma1d9ijGkJ"

    private fun provideClient() : OkHttpClient{
            val headerInterceptor = Interceptor {
                val original: Request = it.request()

                val request: Request = original.newBuilder()
                    .header("x-api-key", HEADER_X_API_KEY)
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
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .client(provideClient())
        .addConverterFactory(GsonConverterFactory.create())

    val retrofit = builder.build()
    val blogApiService: BlogApiService = retrofit.create(BlogApiService::class.java)
}