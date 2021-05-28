package com.example.mvvm_retrofit_room.view.fragment

import android.util.Log
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.databinding.FragmentBlogListBinding
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.InternetConnection
import com.example.mvvm_retrofit_room.view.adapter.BlogAdapter
import com.example.mvvm_retrofit_room.view.base.BaseFragment
import com.example.mvvm_retrofit_room.view.listener.BlogListener
import com.example.mvvm_retrofit_room.viewmodel.BlogListFragmentViewModel


class BlogListFragment : BaseFragment<FragmentBlogListBinding, BlogListFragmentViewModel>(),
    BlogListener {

    private lateinit var mBlogAdapter: BlogAdapter
    private var mBlog = Blog()

    override fun getLayoutId(): Int = R.layout.fragment_blog_list

    override fun getViewModel(): Class<BlogListFragmentViewModel> =
        BlogListFragmentViewModel::class.java

    override fun onViewReady() {
        binding.handleBlogListFrmEvent = this

        setHasOptionsMenu(true)
        setToolbarTitle(getString(R.string.toolbar_title_list_blog))

        viewModel.setBlogListener(this)

        mBlogAdapter = BlogAdapter(viewModel)
        binding.rvUserList.adapter = mBlogAdapter

        refreshData()
        binding.swiperLayout.setOnRefreshListener {
            refreshData()
        }

        dataListener()
    }

    //chuyển sang fragment edit/delete
    override fun onBlogClicked(blog: Blog) {
        goToExecuteFragment(blog = blog)
    }

    //chuyển sang fragment add
    fun addNewBlog() {
        goToExecuteFragment(blog = mBlog)
    }

    private fun goToExecuteFragment(blog: Blog){
        val action =
            BlogListFragmentDirections.actionBlogListFragmentToBlogExecuteFragment(blog)
        view?.let { Navigation.findNavController(it).navigate(action) }
    }

    private fun dataLoadingStateListner() {
        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            binding.swiperLayout.isRefreshing = it.isDataLoading
        })
    }

    private fun dataListener(){
        viewModel.blogs.observe(viewLifecycleOwner, Observer {
            mBlogAdapter.submitData(it)
        })
    }

    //lấy data trên server
    private fun refreshData() {
        dataLoadingStateListner()
        viewModel.getAllBlogFromServer()
    }
}