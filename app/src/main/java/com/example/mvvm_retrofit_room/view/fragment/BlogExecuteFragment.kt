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
import com.example.mvvm_retrofit_room.utils.ImagePath
import com.example.mvvm_retrofit_room.utils.InternetConnection
import com.example.mvvm_retrofit_room.utils.Status
import com.example.mvvm_retrofit_room.utils.loadImage
import com.example.mvvm_retrofit_room.view.base.BaseFragment
import com.example.mvvm_retrofit_room.viewmodel.ExecuteBlogFragmentViewModel
import com.example.myapplication.utils.ReusableFunctionForEdittext.hideKeyboardInFragment
import com.google.android.material.textfield.TextInputEditText
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import okhttp3.MultipartBody
import java.io.File


class BlogExecuteFragment :
    BaseFragment<FragmentBlogExecuteBinding, ExecuteBlogFragmentViewModel>() {

    private val mArgs: BlogExecuteFragmentArgs by navArgs();
    private lateinit var mBlog: Blog
    private lateinit var mEditTextList: ArrayList<TextInputEditText>

    private var mUploadURL: String? = null
    private var mBlogImageURI: Uri? = null
    private lateinit var mImageFile: File

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
        viewModel.getBlogUploadableURL(mImageFile.name).observe(viewLifecycleOwner, Observer {
            it?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        customProgressDialog.dismiss()
                        resource.data?.let {
                            mUploadURL = it.blogImageUploadURL.split("amazonaws.com/")[1]
                            putImageToServer(uploadURL = it.blogImageUploadURL,)
                            Log.e("uploadURL ", mUploadURL.toString())
                        }
                    }
                    Status.ERROR -> {
                        if (!InternetConnection.isOnline(requireContext())) {
                            showToast("Error: No internet connection")
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

        /*if (isTextFullfill(getAllEditText(binding.relativeContainer, mEditTextList))) {
            getNewBlog()
            viewModel.addNewBlogToServer(mBlog).observe(viewLifecycleOwner, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            customProgressDialog.dismiss()
                            showToast("Blog Added")
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
        } else {
            showToast("Error: Invalid text")
        }
        mEditTextList.clear()
        clearAllEdittext(binding.relativeContainer)
        view?.let { activity?.hideKeyboardInFragment(it) }*/
    }

    fun putImageToServer(uploadURL: String) {
        viewModel.uploadBlogImageToServer(uploadURL = uploadURL, imageFile = mImageFile)
        viewModel.isImageUploaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                Log.e("uploaded", "success")
                Log.e("url =: ", uploadURL)
                val imageURL = uploadURL.split("?X-Amz-Algorithm")[0]
                mBlog.blogImageURL = imageURL
                loadImage(binding.ivBlogImage, imageURL)
            } else {
                Log.e("uploaded", "fail")
            }
            customProgressDialog.dismiss()
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
        mBlog.blogImageURL = "any string"
    }

    fun onImageClicked() {
        validatePermissions()
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun validatePermissions() {
        Dexter.withContext(activity)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    openGalleryForImage()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
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
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {

            data?.data.let {
                mBlogImageURI = it
                mImageFile = File(ImagePath.getPathFromUri(requireContext(), it!!))

                //loadImage(binding.ivBlogImage, mImageFile.toString())
            }
        }
    }


}