package com.example.forwarding

import android.Manifest
import android.app.Activity
import android.app.ActivityManager
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
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.prefs.Preferences


class MainActivity : AppCompatActivity() {

    lateinit var tvLastGivenPassword: TextView
    lateinit var etPassword: EditText

    lateinit var userManager: UserManager
    var password = ""

    var br = object: BroadcastReceiver(){
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onReceive(context: Context?, p1: Intent?) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){

                for(sms:SmsMessage in Telephony.Sms.Intents.getMessagesFromIntent(p1)){

                    val text:String? = sms.displayMessageBody
                    var delimiter = " "

                    val parts = text?.split(delimiter)
                    val first = parts?.get(0).toString()   //yonlendir anahtar kelime
                    val second = parts?.get(1).toString()  //tel no
                    val third = parts?.get(2).toString()   //sifre anahtar kelime
                    val fourth = parts?.get(3).toString()  //sifre

                    Toast.makeText(applicationContext,fourth,Toast.LENGTH_LONG).show()

                    val lastGivenPassword = findViewById<TextView>(R.id.lastGivenPassword).text.toString()

                    //yonlendirme if'i
                    if(first == "yonlendir" && contactExists(context!!,second) && third == "sifre" && fourth.equals(lastGivenPassword)){
                        //val intent:Intent = Intent(context,MainActivity::class.java)
                        callForward(context,second)
                        //startActivity(intent)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btn = findViewById<Button>(R.id.btn_action)
        registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))

        val btnSave = findViewById<Button>(R.id.btn_save)
        tvLastGivenPassword = findViewById(R.id.lastGivenPassword)

        userManager = UserManager(this)

        buttonSave(btnSave)

        observeData()

        if(checkPermissions()) {
            val key = intent.getBooleanExtra("key",false)

            requestRequest()
        }
        else{
            checkAndRequestPermissions()
        }

        val serviceIntent: Intent = Intent(this,MyAndroidService::class.java)

        if(isMyServiceRunning(MyAndroidService::class.java)){
            btn.text = "Stop"
        }
        else {
            btn.text = "Start"
        }
        btn.setOnClickListener{
            if(isMyServiceRunning(MyAndroidService::class.java)){
                stopService(serviceIntent)
                btn.text = "Start"
            }
            else {
                startService(serviceIntent)
                btn.text = "Stop"
            }
        }
    }

    @SuppressWarnings("deprecation")
    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun checkAndRequestPermissions() {
        val permissionsReceiveMessage: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)
        val permissionsCallPhone: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
        val permissionsReadContacts: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)

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


        if(listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 111)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var isDeny = false
        grantResults.forEach {
            if (it == -1) {
                isDeny = true
                return@forEach
            }
        }
        if (isDeny){
            checkAndRequestPermissions()
        } else {
            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("key",true)
            startActivity(intent)
        }
        /*if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            receiveMsg()
        }*/
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
/*            setNegativeButton("Hayır") { _, _ ->
                Toast.makeText(context,"Hayır",Toast.LENGTH_SHORT)
            }*/

        }.create().show()
    }

    private fun showDefaultDialogLower(context:Context,intent:Intent){
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

    private fun requestRequest(){
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
            }

        }
        else{                                                           //Android 9 ve Altı
            if(!Settings.canDrawOverlays(this)){                //Draw overlay izni yoksa

                val intentAlert = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                showDefaultDialogLower(this,intentAlert)
                startActivity(intentAlert)

            }
            else{                                                       //draw overlay izni varsa
            }

        }
    }

    private fun checkPermissions(): Boolean {
        val permissionsReceiveMessage: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)
        val permissionsCallPhone: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE)
        val permissionsReadContacts: Int = ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS)

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

        return listPermissionsNeeded.isEmpty()

    }

    private fun buttonSave(btnSave:Button){
        btnSave.setOnClickListener{
            var tvLastGivenPassword = findViewById<TextView>(R.id.lastGivenPassword)
            etPassword = findViewById<EditText>(R.id.password)
            val pass = etPassword.text.toString()
            if(pass.equals("")){
                buttonSave(btnSave)
            }
            else{
                tvLastGivenPassword.text = pass

                GlobalScope.launch {
                    userManager.storeUser(pass)
                }}

        }
    }

    private fun observeData() {
        // Updates name
        // every time user name changes it will be observed by userNameFlow
        // here it refers to the value returned from the usernameFlow function
        // of UserManager class
        userManager.userPasswordFlow.asLiveData().observe(this) {
            password = it!!
            tvLastGivenPassword.text = it.toString()
        }
    }
}
