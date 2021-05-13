package com.example.mvvm_retrofit_room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "blog_table")
data class Blog(
    @SerializedName("title")
    @ColumnInfo(name = "blog_title_col") var blogTitle: String = "",

    @SerializedName("description")
    @ColumnInfo(name = "blog_descripion_col") var blogDescription: String = "",

    @SerializedName("imageUrl")
    @ColumnInfo(name = "blog_image_url") var blogImageURL: String = ""

) : Serializable {
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "blog_id_col")
    var blogID: String = ""
}