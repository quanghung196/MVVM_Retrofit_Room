package com.example.mvvm_retrofit_room.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.example.mvvm_retrofit_room.R
import com.example.mvvm_retrofit_room.utils.InternetConnection
import java.lang.NullPointerException

/*
* Class Network receiver sử dụng để lắng nghe trạng thái kết nối internet của device
* */
class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var isNetworkConnected = InternetConnection.isOnline(context!!)

        try {
            if (isNetworkConnected) {
                Toast.makeText(context, context.getString(R.string.status_network_connected), Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, context.getString(R.string.status_network_disconnected), Toast.LENGTH_SHORT).show()
            }
        } catch (npe: NullPointerException) {
            npe.printStackTrace()
        }
    }

}