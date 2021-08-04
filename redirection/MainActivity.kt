package com.example.forwarding

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract.PhoneLookup
import android.provider.Telephony
import android.telecom.CallRedirectionService
import android.telecom.PhoneAccountHandle
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ActivityCompat.checkSelfPermission
                (this, Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS),111
            )
        }
        else
            receiveMsg()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            receiveMsg()
        }
    }

    private fun receiveMsg() {
        var br = object: BroadcastReceiver(){
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun onReceive(context: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms:SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                        //Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_LONG).show()
                        val text:String? = sms.displayMessageBody
                        var delimiter = " "

                        val parts = text?.split(delimiter)
                        val first = parts?.get(0).toString()   //yonlendir anahtar kelime
                        val second = parts?.get(1).toString()  //tel no
                        val third = parts?.get(2).toString()   //sifre anahtar kelime
                        val fourth = parts?.get(3).toString()  //sifre

                        Toast.makeText(applicationContext,first,Toast.LENGTH_LONG).show()
                        Toast.makeText(applicationContext,second,Toast.LENGTH_LONG).show()
                        Toast.makeText(applicationContext,third,Toast.LENGTH_LONG).show()
                        Toast.makeText(applicationContext,fourth,Toast.LENGTH_LONG).show()

                        //yonlendirme if'i
                        if(first == "yonlendir" && contactExists(context!!,second) && third == "sifre" && fourth == "123456"){
                            Toast.makeText(applicationContext,"Yaay",Toast.LENGTH_LONG).show()
                            val intent = Intent(Intent.ACTION_CALL)
                            var prefix:String = "**21*"
                            //prefix = Uri.encode(prefix)
                            intent.data = Uri.parse("tel:"+prefix+second)
                            if(ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CALL_PHONE
                                ) != PackageManager.PERMISSION_GRANTED){
                                return
                            }
                            startActivity(intent)


                        }
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))


    }
    fun contactExists(context: Context, number: String?): Boolean {
        /// number is the phone number
        val lookupUri: Uri = Uri.withAppendedPath(
            PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(number)
        )
        val mPhoneNumberProjection =
            arrayOf(PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME)
        val cur: Cursor? =
            context.contentResolver.query(lookupUri, mPhoneNumberProjection, null, null, null)
        try {
            if (cur != null) {
                if (cur.moveToFirst()) {
                    if (cur != null) {
                        cur.close()
                    }
                    return true
                }
            }
        } finally {
            if (cur != null) cur.close()
        }
        return false
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    class RedirectionService : CallRedirectionService() {
        override fun onPlaceCall(
            handle: Uri,
            initialPhoneAccount: PhoneAccountHandle,
            allowInteractiveResponse: Boolean
        ) {
            // Determine if the call should proceed, be redirected, or cancelled.
            val callShouldProceed = true
            val callShouldRedirect = false
            when {
                callShouldProceed -> {
                    placeCallUnmodified()
                }
                callShouldRedirect -> {
                    // Update the URI to point to a different phone number or modify the
                    // PhoneAccountHandle and redirect.
                    redirectCall(handle, initialPhoneAccount, true)
                }
                else -> {
                    cancelCall()
                }
            }
        }
    }

}
