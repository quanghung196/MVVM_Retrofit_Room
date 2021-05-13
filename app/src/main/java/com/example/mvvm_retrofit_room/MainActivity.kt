package com.example.mvvm_retrofit_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.mvvm_retrofit_room.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mNavController= Navigation.findNavController(this, R.id.nav_host_fragment)
        //setSupportActionBar(binding.toolbarbMain)
        mBinding.toolbarbMain.setupWithNavController(mNavController)
    }
}