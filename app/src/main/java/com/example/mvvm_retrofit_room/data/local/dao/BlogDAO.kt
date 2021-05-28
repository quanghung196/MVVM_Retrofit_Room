package com.example.mvvm_retrofit_room.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mvvm_retrofit_room.model.Blog
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

/*
* interface BlogDAO chứa các method dùng để truy xuất data trong database
* */
@Dao
interface BlogDAO {

    @Query("select * from blog_table")
    fun getAllBlogFromDatabase(): Flowable<List<Blog>>

    @Insert
    fun synchronizeAllBlogFromServer(blogs: List<Blog>): Completable

    @Query("delete from blog_table")
    fun deteleAllBlogFromDatabase(): Completable
}