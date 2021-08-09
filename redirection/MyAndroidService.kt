package com.example.forwarding

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

class MyAndroidService: Service() {

    override fun onCreate() {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val CHANNEL_ID: String = "exampleServiceChannel"

        val notificationIntent:Intent = Intent(this,MainActivity::class.java)
        val pendingIntent:PendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0)


        val notification: Notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Call Forwarding")
            .setContentText("Working in the Background")
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1,notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}
