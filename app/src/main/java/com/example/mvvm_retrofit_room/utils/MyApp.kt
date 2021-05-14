package com.example.mvvm_retrofit_room.utils

import android.app.Application

class MyApp : Application() {
    init {
        instance = this
    }
    companion object{
        private var instance : MyApp? = null
        fun getInstance() : MyApp {
            return instance!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}