package com.example.mvvm_retrofit_room.view.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.databinding.FragmentBlogExecuteBinding
import com.example.mvvm_retrofit_room.model.Blog
import com.example.mvvm_retrofit_room.utils.Constants.GALLERY_REQUEST_CODE
import com.example.mvvm_retrofit_room.utils.InternetConnection
import com.example.mvvm_retrofit_room.utils.Status
import com.example.mvvm_retrofit_room.utils.loadImage
import com.example.mvvm_retrofit_room.view.base.BaseFragment
import com.example.mvvm_retrofit_room.viewmodel.ExecuteBlogFragmentViewModel
import com.example.myapplication.utils.ReusableFunctionForEdittext.clearAllEdittext
import com.example.myapplication.utils.ReusableFunctionForEdittext.getAllEditText
import com.example.myapplication.utils.ReusableFunctionForEdittext.hideKeyboardInFragment
import com.example.myapplication.utils.ReusableFunctionForEdittext.isTextFullfill
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener


class BlogExecuteFragment :
    BaseFragment<FragmentBlogExecuteBinding, ExecuteBlogFragmentViewModel>() {

    private val mArgs: BlogExecuteFragmentArgs by navArgs();
    private lateinit var mBlog: Blog
    private lateinit var mEditTextList: ArrayList<TextInputEditText>
    private var mUploadableURL: String? = null
    private var mBlogImageURI: Uri? = null

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
            //binding.ivBlogImage.isEnabled = false
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
        getUploadableURL()
        mEditTextList.clear()
        clearAllEdittext(binding.relativeContainer)
    }

    private fun addBlogData(){
        getNewBlog()
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
                        if (!InternetConnection.isOnline(requireContext())) {
                            showToast("Error: No internet connection")
                        } else {
                            it.message?.let { it -> showToast(it) }
                        }
                        customProgressDialog.dismiss()
                    }
                    Status.LOADING -> {
                        customProgressDialog.setTitle("Adding, please wait...")
                        customProgressDialog.show()
                    }
                }
            }
        })
    }

    private fun getUploadableURL() {
        if (isTextFullfill(getAllEditText(binding.relativeContainer, mEditTextList))) {
            viewModel.getBlogUploadableURL().observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            customProgressDialog.dismiss()
                            resource.data?.let {
                                mBlog.blogImageURL = it.blogImageUploadURL
                                uploadBlogImageToServer(
                                    it.blogImageUploadURL,
                                    mBlogImageURI.toString()
                                )
                            }
                        }
                        Status.ERROR -> {
                            if (!InternetConnection.isOnline(requireContext())) {
                                showToast("Error: No internet connection")
                            } else {

                            }
                            customProgressDialog.dismiss()
                        }
                        Status.LOADING -> {
                            customProgressDialog.setTitle("Getting, please wait...")
                            customProgressDialog.show()
                        }
                    }
                }

            })
        }else {
            showToast("Error: Invalid text")
        }
    }

    private fun uploadBlogImageToServer(uploadURL: String, imageUri: String) {
        viewModel.uploadBlogImageToServer(uploadURL, imageUri)
        viewModel.isImageUploaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                showToast("aaaaaaaaaaaaaaa")
                addBlogData()
            }
        })
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
                        if (!InternetConnection.isOnline(requireContext())) {
                            showToast("Error: No internet connection")
                        } else {
                            customProgressDialog.dismiss()
                            showToast("Blog Deleted")
                            view?.let { activity?.hideKeyboardInFragment(it) }
                            backToBlogListFragment()
                        }
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

    fun onImageClicked() {
        validatePermissions()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun validatePermissions() {
        Dexter.withContext(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    //showToast("Permission granted")
                    openGalleryForImage()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: com.karumi.dexter.listener.PermissionRequest?,
                    token: PermissionToken?
                ) {
                    if (token != null) {
                        token.continuePermissionRequest()
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    showToast("Permission Denied")
                }

            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            mBlogImageURI = data.data
            loadImage(binding.ivBlogImage, data.data.toString())
        }
    }
}