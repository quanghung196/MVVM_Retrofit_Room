package com.example.mvvm_retrofit_room.view.fragment

import android.content.ContentValues.TAG
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.databinding.FragmentBlogExecuteBinding
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.Status
import com.example.mvvm_retrofit_room.view.base.BaseFragment
import com.example.mvvm_retrofit_room.view.customview.CustomProgressDialog
import com.example.mvvm_retrofit_room.viewmodel.BlogListFragmentViewModel
import com.example.mvvm_retrofit_room.viewmodel.ExecuteBlogFragmentViewModel
import com.example.myapplication.utils.ReusableFunctionForEdittext
import com.example.myapplication.utils.ReusableFunctionForEdittext.clearAllEdittext
import com.example.myapplication.utils.ReusableFunctionForEdittext.getAllEditText
import com.example.myapplication.utils.ReusableFunctionForEdittext.hideKeyboardInFragment
import com.example.myapplication.utils.ReusableFunctionForEdittext.isTextFullfill
import com.google.android.material.textfield.TextInputEditText


class BlogExecuteFragment :
    BaseFragment<FragmentBlogExecuteBinding, ExecuteBlogFragmentViewModel>() {

    private val mArgs: BlogExecuteFragmentArgs by navArgs();
    private lateinit var mBlog: Blog
    private lateinit var mEditTextList: ArrayList<TextInputEditText>

    override fun getLayoutId(): Int = R.layout.fragment_blog_execute

    override fun getViewModel(): Class<ExecuteBlogFragmentViewModel> =
        ExecuteBlogFragmentViewModel::class.java

    override fun onViewReady() {
        mBlog = mArgs.blog

        binding.blog = mBlog
        binding.viewModel = viewModel
        binding.handleBlogExecuteFrmEvent = this

        mEditTextList = ArrayList()
        if (mBlog.blogTitle.length > 0) {
            setToolbarTitle("Edit Blog")
            binding.btnAdd.visibility = View.GONE
        } else {
            setToolbarTitle("Add Blog")
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
        }
    }

    //về fragment list
    fun backToBlogListFragment() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_blogExecuteFragment_to_blogListFragment)
        }
    }

    //add data lên server
    fun addNewBlog() {
        getNewBlog()
        mEditTextList.clear()
        if (isTextFullfill(getAllEditText(binding.relativeContainer, mEditTextList))) {
            mBlog.blogImageURL = "any string"
            viewModel.addNewBlogToServer(mBlog).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            customProgressDialog.dismiss()
                            showToast("Blog Added")
                            view?.let { activity?.hideKeyboardInFragment(it) }
                            backToBlogListFragment()
                        }
                        Status.ERROR -> {
                            customProgressDialog.dismiss()
                            it.message?.let { it -> showToast(it) }
                        }
                        Status.LOADING -> {
                            customProgressDialog.setTitle("Adding, please wait...")
                            customProgressDialog.show()
                        }
                    }
                }
            })
        } else {
            showToast("Error: Invalid text")
        }
        clearText()
    }

    //xóa data trên server
    fun deleteCurrentBlog() {
        viewModel.deleteCurrentBlog(mBlog.blogID).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        customProgressDialog.dismiss()
                        showToast("Deleted fail")
                    }
                    Status.ERROR -> {
                        customProgressDialog.dismiss()
                        showToast("Blog Deleted")
                        view?.let { activity?.hideKeyboardInFragment(it) }
                        backToBlogListFragment()
                    }
                    Status.LOADING -> {
                        customProgressDialog.setTitle("Deleting, please wait...")
                        customProgressDialog.show()
                    }
                }
            }
        })
    }

    //get text từ edittext
    private fun getNewBlog() {
        mBlog.blogTitle = binding.titBlogTitle.text.toString()
        mBlog.blogDescription = binding.titBlogDescription.text.toString()
    }

    private fun clearText() {
        clearAllEdittext(binding.relativeContainer)
    }

    fun onImageClicked() {
        showToast("Clicked")
    }
}