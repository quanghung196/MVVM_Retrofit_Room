package com.example.mvvm_retrofit_room.view.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
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
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.File


class BlogExecuteFragment :
    BaseFragment<FragmentBlogExecuteBinding, ExecuteBlogFragmentViewModel>() {

    private val mArgs: BlogExecuteFragmentArgs by navArgs();
    private lateinit var mBlog: Blog
    private lateinit var mEditTextList: ArrayList<TextInputEditText>

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
            setToolbarTitle(getString(R.string.toolbar_title_edit_blog))
            binding.btnAdd.visibility = View.GONE
        } else {
            setToolbarTitle(getString(R.string.toolbar_title_add_blog))
            binding.btnDelete.visibility = View.GONE
            binding.btnEdit.visibility = View.GONE
        }

        accessStateListener()
        blogUploadableURLListener()
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
        if (isTextFullfill(
                getAllEditText(
                    binding.relativeContainer,
                    mEditTextList
                )
            ) && mImageFile != null
        ) {
            customProgressDialog.setTitle(getString(R.string.cpd_title_add))
            customProgressDialog.show()

            viewModel.getBlogUploadableURL(mImageFile.name)
        } else {
            showToast(getString(R.string.msg_error_invalid_text))
            mEditTextList.clear()
            clearAllEdittext(binding.relativeContainer)
        }
    }

    //lấy url để upload ảnh
    private fun blogUploadableURLListener() {
        viewModel.blogUploadableURL.observe(viewLifecycleOwner, Observer {
            val uploadURL = it.blogImageUploadURL
            if(uploadURL.length > 0){
                putImageToServer(uploadURL)
            }else{
                customProgressDialog.dismiss()
            }
        })
    }

    //xóa data trên server
    fun deleteCurrentBlog() {
        customProgressDialog.setTitle(getString(R.string.cpd_title_delete))
        customProgressDialog.show()

        viewModel.deleteCurrentBlog(mBlog.blogID)
    }

    //upload ảnh lên server
    fun putImageToServer(uploadURL: String) {
        viewModel.uploadBlogImageToServer(uploadURL = uploadURL, imageFile = mImageFile)
        viewModel.isImageUploaded.observe(viewLifecycleOwner, Observer {
            if (it) {
                val imageURL = uploadURL.split("?X-Amz-Algorithm")[0]
                mBlog.blogImageURL = imageURL
                getNewBlog()
                viewModel.addNewBlogToServer(mBlog)
            } else {
                customProgressDialog.dismiss()
            }
        })
    }

    //state khi thêm hoặc xóa data (success/failure)
    private fun accessStateListener() {
        viewModel.remoteDataAccessState.observe(viewLifecycleOwner, Observer {
            view?.let { activity?.hideKeyboardInFragment(it) }
            customProgressDialog.dismiss()
            if (it) {
                backToBlogListFragment()
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
                    showToast(getString(R.string.msg_permission_denied))
                }

            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.data.let {
                mImageFile = File(ImagePath.getPathFromUri(requireContext(), it!!))
                loadImage(binding.ivBlogImage, mImageFile.toString())
            }
        }
    }
}