package com.example.mvvm_retrofit_room.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class BlogImageUploadURL(
    @SerializedName("uploadUrl")
    var blogImageUploadURL: String = "",

    @SerializedName("fileKey")
    var blogFileKey: String = "")