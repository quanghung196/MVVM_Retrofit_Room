package com.example.mvvm_retrofit_room.utils

import android.annotation.SuppressLint
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvm_retrofit_room.R
import com.makeramen.roundedimageview.RoundedImageView

@BindingAdapter("imageURL")
fun loadImage(view: RoundedImageView, profileImage: String) {

    Glide.with(view.context)
        .load(profileImage)
        .placeholder(R.drawable.image_place_holder)
        .error(if (profileImage.length == 0) R.drawable.image_place_holder else R.drawable.image_error)
        .into(view)
}

