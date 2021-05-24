package com.example.mvvm_retrofit_room.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.mvvm_retrofit_room.data.repository.BlogRepository
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.MyApp
import com.example.mvvm_retrofit_room.utils.Resource
import kotlinx.coroutines.Dispatchers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


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
            emit(Resource.success(data = mBlogRepository.addBlogToServer(blog = blog)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    //xóa data trên server
    fun deleteCurrentBlog(blogID: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            mBlogRepository.deteteBlogOnServer(blogID)
            emit(Resource.success(data = mBlogRepository.getBlogFromServerByID(blogID = blogID)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    //get image upload url
    fun getBlogUploadableURL(fileName: String) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = mBlogRepository.getBlogUploadableURL(fileName = fileName)))
        } catch (exception: Exception) {
            emit(Resource.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
    }

    fun uploadBlogImageToServer(uploadURL: String, imageFile: File) {
        val MEDIA_TYPE_IMAGE: MediaType? = "image/*".toMediaTypeOrNull()

        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                imageFile.name, imageFile.name,
                imageFile.asRequestBody(MEDIA_TYPE_IMAGE)
            )
            .build()

        mBlogRepository
            .putImageToServer(url = uploadURL, image = requestBody)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.code() == 200) {
                        _isImageUploaded.postValue(true)
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("failure", t.message.toString())
                    _isImageUploaded.postValue(false)
                }
            })
    }
}

