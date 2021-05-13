package com.example.mvvm_retrofit_room.utils

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvm_retrofit_room.R
import com.makeramen.roundedimageview.RoundedImageView

@BindingAdapter("imageURL")
fun loadImage(view: RoundedImageView, profileImage: String) {

    Glide.with(view.context)
        .load(profileImage)
        .placeholder(R.drawable.image_place_holder)
        .error(R.drawable.image_error)
        .into(view)
}