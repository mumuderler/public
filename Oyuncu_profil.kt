package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.icu.text.AlphabeticIndex
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_oyuncu_profil.*
import kotlinx.android.synthetic.main.activity_oyuncu_profil.view.*
import kotlinx.android.synthetic.main.oyuncu_karti.view.*
import org.w3c.dom.Text

class Oyuncu_profil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_oyuncu_profil)

        var bundle: Bundle? = intent.extras
        var ad : String? = bundle?.getString("constAd")
        var takim : String? = bundle?.getString("constTakim")
        var resim : Int? = bundle?.getInt("constResim")

        profilAd.text = ad
        profilTakim.text = takim
        profilResim.setImageResource(resim!!)

        geriButonu.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

"""
        fun addRecord(view: View){
            val goals = etGoals.text.toString()
            val assists = etAssists.text.toString()

            //database

            if(!goals.isEmpty() && !assists.isEmpty()){

            }
            else{
                Toast.makeText(applicationContext, "Goal and Assists cannot be blank", Toast.LENGTH_SHORT).show()
            }
        }

        class RecordAdapter: BaseAdapter {
            var constList=ArrayList<RecordModelClass>()
            var context: Context?=null

            constructor(context: Context, constList:ArrayList<RecordModelClass>):super(){
                this.context=context
                this.constList=constList
            }
            override fun getCount(): Int {
                return constList.size
            }

            override fun getItem(position: Int): Any {
                return constList[position]
            }

            override fun getItemId(position: Int): Long {
                return position.toLong()
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                var posRecords=constList[position]
                var inflator= context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                var recordKarti=inflator.inflate(R.layout.activity_oyuncu_profil,null)

                val kartGoals = recordKarti.findViewById(R.id.etGoals) as TextView
                kartGoals.text = posRecords.constGoals.toString()
                val kartAssists = recordKarti.findViewById(R.id.etAssists) as TextView
                kartAssists.text = posRecords.constAssists.toString()




                recordKarti.saveButton.setOnClickListener{
                    addRecord(convertView!!)
                }

                return recordKarti
            }


        }
"""



    }
}