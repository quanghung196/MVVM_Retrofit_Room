package com.example.mvvm_retrofit_room.view.base

import android.app.Activity
import android.content.Context
import android.os.Bundle

import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mvvm_retrofit_room.view.customview.CustomProgressDialog
import com.example.mvvm_retrofit_room.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<V : ViewDataBinding, VM : ViewModel> : Fragment(){
    protected lateinit var binding: V
    protected lateinit var viewModel: VM

    protected lateinit var customProgressDialog: CustomProgressDialog

    abstract fun getLayoutId(): Int
    abstract fun getViewModel(): Class<VM>
    abstract fun onViewReady()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this

        viewModel = ViewModelProvider(this, ViewModelFactory(requireContext())).get(getViewModel())

        customProgressDialog = CustomProgressDialog(activity as Activity)

        return binding.root
    }

    fun showToast(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    fun setToolbarTitle(title: String) {
        (activity as Activity).toolbarbMain.title = title
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewReady()
    }
}