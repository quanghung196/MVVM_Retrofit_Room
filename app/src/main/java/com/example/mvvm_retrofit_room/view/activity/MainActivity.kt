package com.example.mvvm_retrofit_room.view.activity


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mNavController: NavController
    //private lateinit var mBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment)
        //setSupportActionBar(binding.toolbarbMain)
        mBinding.toolbarbMain.setupWithNavController(mNavController)

        /*mBroadcastReceiver = NetworkReceiver()
        registerBroadcast()
        getDeviceToken()*/
    }

    /*private fun getDeviceToken(): String {
        var fbToken = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isComplete) {
                fbToken = it.result.toString()
                Log.e("fbToken", fbToken)
            }
        }
        return fbToken
    }

    private fun registerBroadcast(){
        registerReceiver(mBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun unregisterBroadcast(){
        unregisterReceiver(mBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterBroadcast()
    }*/
}