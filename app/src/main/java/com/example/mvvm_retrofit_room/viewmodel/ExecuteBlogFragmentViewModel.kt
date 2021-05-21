package com.example.mvvm_retrofit_room.viewmodel

import android.content.ContentValues
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
import java.net.URI.create


class ExecuteBlogFragmentViewModel : ViewModel() {

    private val mBlogRepository: BlogRepository = BlogRepository()
    private val mContext: Context = MyApp.getInstance()

    private val _isImageUploaded = MutableLiveData<Boolean>()
    val isImageUploaded : LiveData<Boolean>
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

    fun uploadBlogImageToServer(uploadURL: String, imageUri: String) {
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
        try {
            var response: Response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                Log.d("lalaa", response.toString())
                _isImageUploaded.postValue(false)
            } else {
                _isImageUploaded.postValue(true)
                Log.e("error", "error")
                /*var respBody : String? = response.body()?.string()
                var jsonObj : JSONObject = JSONObject(respBody)
                var url : String = jsonObj.getString("url")
                var id: String = jsonObj.getString("id")*/
            }
        } catch (e: IOException) {
            e.message?.let { Log.e("error", it) }
            _isImageUploaded.postValue(false)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i(ContentValues.TAG, "onCleared: ")
    }
}