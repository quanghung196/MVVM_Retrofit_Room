package com.example.mvvm_retrofit_room.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.data.repository.BlogRepository
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.model.BlogImageUploadURL
import com.example.mvvm_retrofit_room.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class ExecuteBlogFragmentViewModel(val context: Context) : ViewModel() {

    private val mBlogRepository: BlogRepository = BlogRepository()
    //private val mContext: Context = MyApp.getInstance()

    private val _isImageUploaded = MutableLiveData<Boolean>()
    val isImageUploaded: LiveData<Boolean>
        get() = _isImageUploaded

    private val _remoteDataAccessState = MutableLiveData<Boolean>()
    val remoteDataAccessState: LiveData<Boolean>
        get() = _remoteDataAccessState

    private val _blogUploadableURL = MutableLiveData<BlogImageUploadURL>()
    val blogUploadableURL: LiveData<BlogImageUploadURL>
        get() = _blogUploadableURL

    init {
        _isImageUploaded.postValue(false)
    }

    //add data lên server
    fun addNewBlogToServer(blog: Blog) {
        mBlogRepository.addBlogToServer(blog = blog)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    setAccessState(isSuccessful = true)
                    showToastMessage(context.getString(R.string.msg_blog_add_success))
                }

                override fun onError(e: Throwable) {
                    setAccessState(isSuccessful = false)
                    showToastMessage(context.getString(R.string.msg_blog_add_fail) + "\n" + e.message)
                }
            })
    }

    //xóa data trên server
    fun deleteCurrentBlog(blogID: String) {
        mBlogRepository.deteteBlogOnServer(blogID = blogID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    setAccessState(isSuccessful = true)
                    showToastMessage(context.getString(R.string.msg_blog_delete_success))
                }

                override fun onError(e: Throwable) {
                    setAccessState(isSuccessful = false)
                    showToastMessage(context.getString(R.string.msg_blog_delete_fail) + "\n" + e.message)
                }
            })
    }

    private fun setAccessState(isSuccessful: Boolean) {
        _remoteDataAccessState.postValue(isSuccessful)
    }

    //get image upload url
    fun getBlogUploadableURL(fileName: String) {
        mBlogRepository
            .getBlogUploadableURL(fileName = fileName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> onBlogUploadableURLResponse(response) },
                { throwawble -> onFailure(throwawble) })
    }

    private fun onBlogUploadableURLResponse(response: BlogImageUploadURL) {
        _blogUploadableURL.postValue(response)
    }

    private fun onFailure(throwawble: Throwable) {
        _blogUploadableURL.postValue(BlogImageUploadURL("", ""))
        showToastMessage(throwawble.message.toString())
    }

    //upload image
    fun uploadBlogImageToServer(uploadURL: String, imageFile: File) {
        val MEDIA_TYPE_IMAGE: MediaType? = "image/*".toMediaTypeOrNull()
        val uploadFile = imageFile.asRequestBody(MEDIA_TYPE_IMAGE)

        mBlogRepository
            .putImageToServer(url = uploadURL, imageFile = uploadFile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    _isImageUploaded.postValue(true)
                }

                override fun onError(e: Throwable) {
                    _isImageUploaded.postValue(false)
                }
            })
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

