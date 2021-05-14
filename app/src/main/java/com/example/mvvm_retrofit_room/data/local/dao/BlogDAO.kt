package com.example.mvvm_retrofit_room.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mvvm_retrofit_room.model.Blog

@Dao
interface BlogDAO {

    @Query("select * from blog_table")
    fun getAllBlogFromDatabase(): LiveData<List<Blog>>

    @Insert
    suspend fun synchronizeAllBlogFromServer(blogs: List<Blog>)

    @Query("delete from blog_table")
    suspend fun deteleAllBlogFromDatabase()
}