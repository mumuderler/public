package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.oyuncu_karti.view.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    var constList = ArrayList<Oyuncular>()
    var adapter:OyuncuAdapter?=null


    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setContentView(R.layout.activity_main)

        constList.add(Oyuncular("Kaka", "Milan", R.drawable.kaka))
        constList.add(Oyuncular("Ronaldo", "Real Madrid", R.drawable.ronaldo))
        constList.add(Oyuncular("Messi", "Barcelona", R.drawable.messi))
        constList.add(Oyuncular("Ozil", "Arsenal", R.drawable.ozil))


        adapter = OyuncuAdapter(this, constList)
        listView.adapter = adapter


    }
    class OyuncuAdapter: BaseAdapter {
        var constList=ArrayList<Oyuncular>()
        var context:Context?=null

        constructor(context:Context,constList:ArrayList<Oyuncular>):super(){
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
            var posOyuncular=constList[position]
            var inflator= context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var oyuncuKarti=inflator.inflate(R.layout.oyuncu_karti,null)

            val kartAdi = oyuncuKarti.findViewById(R.id.kartAdi) as TextView
            kartAdi.text = posOyuncular.constAd
            val kartTakim = oyuncuKarti.findViewById(R.id.kartTakim) as TextView
            kartTakim.text = posOyuncular.constTakim
            val kartResim = oyuncuKarti.findViewById(R.id.kartResim) as ImageView
            oyuncuKarti.kartResim.setImageResource(posOyuncular.constResim!!)

            oyuncuKarti.kartResim.setOnClickListener{
                var intent = Intent(context,Oyuncu_profil::class.java)
                intent.putExtra("constAd",posOyuncular.constAd)
                intent.putExtra("constTakim",posOyuncular.constTakim)
                intent.putExtra("constResim",posOyuncular.constResim!!)
                context!!.startActivity(intent)


            }

            return oyuncuKarti
        }


    }
}