package com.example.mvvm_retrofit_room.view.fragment

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.databinding.FragmentBlogListBinding
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.Status
import com.example.mvvm_retrofit_room.view.adapter.BlogAdapter
import com.example.mvvm_retrofit_room.view.base.BaseFragment
import com.example.mvvm_retrofit_room.view.listener.BlogListener
import com.example.mvvm_retrofit_room.viewmodel.BlogListFragmentViewModel


class BlogListFragment : BaseFragment<FragmentBlogListBinding, BlogListFragmentViewModel>(),
    BlogListener {

    private lateinit var mBlogAdapter: BlogAdapter
    private lateinit var mBlog: Blog

    override fun getLayoutId(): Int = R.layout.fragment_blog_list

    override fun getViewModel(): Class<BlogListFragmentViewModel> =
        BlogListFragmentViewModel::class.java

    override fun onViewReady() {
        binding.handleBlogListFrmEvent = this

        viewModel.setBlogListener(this)

        mBlogAdapter = BlogAdapter(viewModel)
        binding.rvUserList.adapter = mBlogAdapter
        refreshData()

        setToolbarTitle("List User")

        binding.swiperLayout.setOnRefreshListener {
            refreshData()
        }
    }

    override fun onBlogClicked(blog: Blog) {
        val action =
            BlogListFragmentDirections.actionBlogListFragmentToBlogExecuteFragment(
                blog
            )
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

    fun addNewBlog() {
        mBlog = Blog("", "", "any string")
        val action =
            BlogListFragmentDirections.actionBlogListFragmentToBlogExecuteFragment(mBlog)
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

    private fun refreshData() {
        viewModel.getAllBlogFromServer().observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        binding.swiperLayout.isRefreshing = false
                        resource.data?.let { blog -> mBlogAdapter.submitData(blog) }
                    }
                    Status.ERROR -> {
                        binding.swiperLayout.isRefreshing = false
                        showToast("Error loading data")
                    }
                    Status.LOADING -> {
                        binding.swiperLayout.isRefreshing = true
                    }
                }
            }
        })
    }

}