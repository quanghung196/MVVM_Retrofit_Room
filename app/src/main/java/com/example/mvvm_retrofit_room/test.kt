package com.example.mvvm_retrofit_room

import android.util.Log
import com.example.mvvm_retrofit_room.data.remote.RetrofitBuilder
import kotlinx.coroutines.Dispatchers.Main

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import kotlin.concurrent.thread

fun main() {
    val url =
        "https://profile-bucket-dev.s3.ap-southeast-1.amazonaws.com/test?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20210521T205318Z&X-Amz-SignedHeaders=host&X-Amz-Expires=59&X-Amz-Credential=AKIAWE3SQ5MCXDKBGU5M%2F20210521%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=3835f588aaf931ab6dc0033c3e3310c84e6da65cd0be6b9599ab553f2244dbe0"
    val uri = "/document/msf:38"
    uploadBlogImageToServer(url, uri)
}

fun uploadBlogImageToServer(uploadURL: String, imageUri: String) {
        try {
            val client = OkHttpClient()
            val file = File(imageUri)

            val MEDIA_TYPE_JPEG: MediaType? = "image/*".toMediaTypeOrNull()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("data", imageUri, file.asRequestBody(MEDIA_TYPE_JPEG))
                .build()

            var request: Request = Request.Builder()
                .url(uploadURL)
                .header("x-api-key", RetrofitBuilder.HEADER_X_API_KEY)
                .post(requestBody).build()
            var response: Response = client.newCall(request).execute()
            if (response.isSuccessful) {
                print(response.body)
            } else {
                print("error upload image to sv")
            }
        } catch (e: IOException) {
                print(e.message)
        }
}