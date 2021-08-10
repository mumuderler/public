package com.example.forwarding

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
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
import android.provider.Settings
import android.provider.Telephony
import android.telephony.SmsMessage
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(checkAndRequestPermissions()) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {            //Android 10 ve 11

                if (!Settings.canDrawOverlays(this)) {               //Draw Overlay izni yoksa

                    val intentAlert = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
                    { result: ActivityResult ->
                        if (result.resultCode == Activity.RESULT_OK) {
                            Toast.makeText(this,"printed",Toast.LENGTH_SHORT).show()
                        }

                    }
                    showDefaultDialog(this,intentAlert,startForResult)

                }
                else{                                                       //Draw Overlay izni varsa
                    checkDrawOverlayPermission()
                    receiveMsg()
                }

            }
            else{                                                           //Android 9 ve Altı
                if(!Settings.canDrawOverlays(this)){                //Draw overlay izni yoksa

                    val intentAlert = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                   // showDefaultDialogLower(this,intentAlert)
                    startActivity(intentAlert)

                }
                else{                                                       //draw overlay izni varsa
                    receiveMsg()
                }

            }
        }

        val serviceIntent: Intent = Intent(this,MyAndroidService::class.java)
        startService(serviceIntent)

        //Sim kartı bilgisi -Xiaomi'de hata veriyor
        /*
        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            val localSubscriptionManager = SubscriptionManager.from(this)
            if (localSubscriptionManager.activeSubscriptionInfoCount > 1) {
                //if the    re are two sims in dual sim mobile
                val localList: List<*> = localSubscriptionManager.activeSubscriptionInfoList
                val simInfo = localList[0] as SubscriptionInfo
                val simInfo1 = localList[1] as SubscriptionInfo
                val sim1 = simInfo.displayName.toString()
                val sim2 = simInfo1.displayName.toString()
                Toast.makeText(this,sim2+sim1,Toast.LENGTH_LONG)

            } else {
                //if there is 1 sim in dual sim mobile
                val tManager = baseContext
                    .getSystemService(TELEPHONY_SERVICE) as TelephonyManager
                val sim1 = tManager.networkOperatorName
                Toast.makeText(this,sim1,Toast.LENGTH_SHORT)

            }
        } else {
            //below android version 22
            val tManager = baseContext
                .getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val sim1 = tManager.networkOperatorName
            Toast.makeText(this,sim1,Toast.LENGTH_SHORT)

        }*/
        val tManager = baseContext
            .getSystemService(TELEPHONY_SERVICE) as TelephonyManager

// Get carrier name (Network Operator Name)

// Get carrier name (Network Operator Name)
        val carrierName = tManager.networkOperatorName

// Get Phone model and manufacturer name

// Get Phone model and manufacturer name
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        Toast.makeText(this,carrierName+manufacturer+model,Toast.LENGTH_LONG)
        Toast.makeText(this,"zdfkjdsjk",Toast.LENGTH_LONG)

    }

    private fun checkAndRequestPermissions(): Boolean {
        val permissionsReceiveMessage: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)
        val permissionsCallPhone: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
        val permissionsReadContacts: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)
        val permissionsReadPhoneState: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE)


        var listPermissionsNeeded = ArrayList<String>()

        if(permissionsReceiveMessage != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS)
        }
        if(permissionsCallPhone!= PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE)
        }
        if(permissionsReadContacts != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)
        }
        if(permissionsReadPhoneState != PackageManager.PERMISSION_GRANTED){
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE)
        }
        if(listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 111)
            return false
        }
        return true

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

                        //Toast.makeText(applicationContext,first,Toast.LENGTH_LONG).show()
                        //Toast.makeText(applicationContext,second,Toast.LENGTH_LONG).show()
                        //Toast.makeText(applicationContext,third,Toast.LENGTH_LONG).show()
                        Toast.makeText(applicationContext,fourth,Toast.LENGTH_LONG).show()

                        //yonlendirme if'i
                        if(first == "yonlendir" && contactExists(context!!,second) && third == "sifre" && fourth == "123456"){
                            val intent:Intent = Intent(context,MainActivity::class.java)
                            intent.putExtra("phoneNumber",second)
                            callForward(context,second)
                            //startActivity(intent)
                        }
                    }
                }
            }
        }
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))


    }

    private fun callForward(context:Context, number:String){

        val intent = Intent(Intent.ACTION_CALL)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        var prefix:String = "**21*"
        //prefix = Uri.encode(prefix)
        intent.data = Uri.parse("tel:"+prefix+number)
        if(ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED){
            return
        }
        startActivity(intent)

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


    private val REQUEST_CODE:Int = 10101;

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (!Settings.canDrawOverlays(this)) {

            // If not, form up an Intent to launch the permission request
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));

            val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result: ActivityResult ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this,"printed",Toast.LENGTH_SHORT).show()
                }

            }
            startForResult.launch(intent)

            // Launch Intent, with the supplied request code
        }
    }

    private fun showDefaultDialog(context:Context, intent:Intent, startForResult:ActivityResultLauncher<Intent>) {
        val alertDialog = AlertDialog.Builder(this)

        alertDialog.apply {
            setTitle("İzin İsteği")
            setMessage("Uygulamanın arkaplanda çalışması için gerekli izni vermek istiyor musunuz?")

            setPositiveButton("Evet") { _, _ ->
                Toast.makeText(context,"Evet",Toast.LENGTH_SHORT)

                startForResult.launch(intent)
            }
            setNegativeButton("Hayır") { _, _ ->
                Toast.makeText(context,"Hayır",Toast.LENGTH_SHORT)
            }

        }.create().show()
    }

 /*   private fun showDefaultDialogLower(context:Context,intent:Intent){
        val alertDialog = AlertDialog.Builder(context)

        alertDialog.apply{
            setTitle("Alert")
            setMessage("Izin almak için evet Butonuna tıklayın")

            setPositiveButton("Evet") { _, _ ->
                Toast.makeText(context,"clicked positive button",Toast.LENGTH_SHORT)

                startActivity(intent)
            }
            setNegativeButton("Negative") { _, _ ->
                Toast.makeText(context,"clicked negative button",Toast.LENGTH_SHORT)
            }

        }.create().show()
    }
*/

}
