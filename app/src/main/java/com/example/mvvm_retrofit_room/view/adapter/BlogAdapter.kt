package com.example.mvvm_retrofit_room.view.adapter

import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.mvvm_retrofit_room.databinding.ItemBlogBinding
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.view.base.BaseRecyclerViewAdapter
import com.example.mvvm_retrofit_room.viewmodel.BlogListFragmentViewModel
import com.makeramen.roundedimageview.RoundedImageView

class BlogAdapter(val blogListFragmentViewModel: BlogListFragmentViewModel) :
    BaseRecyclerViewAdapter<Blog, ItemBlogBinding>() {

    override val itemLayout: Int = R.layout.item_blog

    override fun bind(binding: ItemBlogBinding, data: Blog, position: Int) {
        val currentBlog =  data
        binding.blog = currentBlog
        binding.viewModel = blogListFragmentViewModel
        binding.executePendingBindings()

    }
}