package com.example.mvvm_retrofit_room.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mvvm_retrofit_room.data.remote.RetrofitBuilder
import com.example.mvvm_retrofit_room.data.repository.BlogRepository
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.MyApp
import com.example.mvvm_retrofit_room.utils.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException


class ExecuteBlogFragmentViewModel : ViewModel() {

    private val mBlogRepository: BlogRepository = BlogRepository()
    private val mContext: Context = MyApp.getInstance()

    private val _isImageUploaded = MutableLiveData<Boolean>()
    val isImageUploaded: LiveData<Boolean>
        get() = _isImageUploaded

    init {
        _isImageUploaded.postValue(false)
    }

    //add data lên server
    fun addNewBlogToServer(blog: Blog) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mBlogRepository.addBlogToServer(blog)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    //xóa data trên server
    fun deleteCurrentBlog(blogID: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            mBlogRepository.deteteBlogOnServer(blogID)
            emit(Resource.success(data = mBlogRepository.getBlogFromServerByID(blogID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    //get image upload url
    fun getBlogUploadableURL() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mBlogRepository.getBlogUploadableURL()))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun uploadBlogImageToServer() {
        val uploadURL = "https://profile-bucket-dev.s3.ap-southeast-1.amazonaws.com/test?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20210523T195802Z&X-Amz-SignedHeaders=host&X-Amz-Expires=59&X-Amz-Credential=AKIAWE3SQ5MCXDKBGU5M%2F20210523%2Fap-southeast-1%2Fs3%2Faws4_request&X-Amz-Signature=a0bd97f1d022dc2873965361eff1e99278606b80596a644d002b8f23a240507a"
        val imageUri = "/storage/emulated/0/Download/img_9.jpg"
        try {
            val client = OkHttpClient()
            val file = File(imageUri)

            val MEDIA_TYPE_IMAGE: MediaType? = "image/*".toMediaTypeOrNull()

            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name)
                .build()

            var request: Request = Request.Builder()
                .url(uploadURL)
                .header("x-api-key", RetrofitBuilder.HEADER_X_API_KEY)
                .post(requestBody)
                .build()

             client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("lala", e.message.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.e("lala", response.body.toString())
                    } else {
                        Log.e("lala", response.message)
                    }
                }
            })

        } catch (e: IOException) {
            Log.e("lalaa", e.message.toString())
        }
    }
}
