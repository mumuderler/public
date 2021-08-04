package com.example.forwarding

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

class App: Application() {
    val CHANNEL_ID: String = "exampleServiceChannel"

    @RequiresApi(Build.VERSION_CODES.O)
    @Override
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            val serviceChannel: NotificationChannel = NotificationChannel(
                CHANNEL_ID,
                "example service channel",
                NotificationManager.IMPORTANCE_DEFAULT,
            )

            val manager:NotificationManager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }

    }


}
