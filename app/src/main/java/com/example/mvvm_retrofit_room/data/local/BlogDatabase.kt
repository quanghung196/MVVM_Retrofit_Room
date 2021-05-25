package com.example.mvvm_retrofit_room.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvvm_retrofit_room.data.local.dao.BlogDAO
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.MyApp

@Database(entities = [Blog::class], version = 3, exportSchema = false)
abstract class BlogDatabase : RoomDatabase() {

    abstract fun getBlogDAO(): BlogDAO

    companion object {
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        @Volatile
        private var instance: BlogDatabase? = null

        fun getInstance(): BlogDatabase {
            if (instance == null) {
                instance = Room
                    .databaseBuilder(
                        MyApp.getInstance(),
                        BlogDatabase::class.java,
                        "BlogDatabase"
                    )
                    //.addMigrations(MIGRATION_2_3)
                    .build()
            }
            return instance!!
        }
    }
}