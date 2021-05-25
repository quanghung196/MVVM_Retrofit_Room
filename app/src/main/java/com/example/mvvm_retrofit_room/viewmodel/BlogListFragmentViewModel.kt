package com.example.mvvm_retrofit_room.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_retrofit_room.data.repository.BlogRepository
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.model.LoadingState
import com.example.mvvm_retrofit_room.view.listener.BlogListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.internal.util.NotificationLite.disposable
import io.reactivex.rxjava3.observers.DisposableCompletableObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class BlogListFragmentViewModel(val context: Context) : ViewModel() {

    private val mBlogRepository: BlogRepository = BlogRepository()
    private lateinit var blogListener: BlogListener

    private val _blogs = MutableLiveData<List<Blog>>()
    val blogs: LiveData<List<Blog>>
        get() = _blogs

    private val _loadingState = MutableLiveData<LoadingState>()
    val loadingState: LiveData<LoadingState>
        get() = _loadingState


    //lấy data từ api
    fun getAllBlogFromServer() {
        _loadingState.postValue(LoadingState(true, false))
        mBlogRepository
            .getAllBlogFromServer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response -> onBlogResponse(response) },
                { throwawble -> onFailure(throwawble) })
    }

    private fun onBlogResponse(response: List<Blog>) {
        Log.e("error", "data response")
        _blogs.postValue(response)
        _loadingState.postValue(LoadingState(false, true))
    }

    private fun onFailure(throwable: Throwable) {
        Log.e("error", "cant receive data")
        getAllBlogFromDatabase()
        showToastMessage(throwable.message.toString())
        _loadingState.postValue(LoadingState(false, false))
    }

    //lấy data từ room
    private fun getAllBlogFromDatabase() {
        Log.e("error", "load local data")
        mBlogRepository
            .getAllBlogFromDatabase()
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                _blogs.postValue(it)
                Log.e("dbListSize",it.size.toString())
            }
    }

    //đồng bộ data từ api vào room
    fun synchronizeAllBlogFromServer(blogs: List<Blog>) {
        mBlogRepository.synchronizeAllBlogFromServer(blogs = blogs)
            .subscribeOn(Schedulers.computation())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    Log.e("dbQuery", "Insert successful")
                }

                override fun onError(e: Throwable) {
                    Log.e("dbQuery", "Insert failure \n" + e.message)
                }
            })
    }

    //xóa hết data trong room
    fun deteleAllBlogFromDatabase() {
        mBlogRepository.deteleAllBlogFromDatabase()
            .subscribeOn(Schedulers.computation())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    Log.e("dbQuery", "Delete successful")
                }

                override fun onError(e: Throwable) {
                    Log.e("dbQuery", "Delete failure \n" + e.message)
                }
            })
    }

    fun setBlogListener(blogListener: BlogListener) {
        this.blogListener = blogListener
    }

    fun onUserClicked(blog: Blog) {
        blogListener.onBlogClicked(blog)
    }

    private fun showToastMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}