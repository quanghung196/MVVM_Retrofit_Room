package com.example.mvvm_retrofit_room.broadcast_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import com.example.mvvm_retrofit_room.utils.InternetConnection
import java.lang.NullPointerException

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        var isNetworkConnected = InternetConnection.isOnline(context!!)

        try {
            if (isNetworkConnected) {
                Toast.makeText(context, "Network connected", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(context, "Network disconnected", Toast.LENGTH_LONG).show()
            }
        } catch (npe: NullPointerException) {
            npe.printStackTrace()
        }
    }

}