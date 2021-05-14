package com.example.mvvm_retrofit_room.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mvvm_retrofit_room.data.local.dao.BlogDAO
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.MyApp

@Database(entities = [Blog::class], version = 1, exportSchema = false)
abstract class BlogDatabase : RoomDatabase() {

    abstract fun getBlogDAO() : BlogDAO

    companion object{
        @Volatile
        private var instance : BlogDatabase? = null

        fun getInstance():BlogDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(MyApp.getInstance(), BlogDatabase::class.java,"BlogDatabase").build()
            }
            return instance!!
        }
    }
}