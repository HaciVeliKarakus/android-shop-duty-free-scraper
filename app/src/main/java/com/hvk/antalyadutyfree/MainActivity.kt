package com.hvk.antalyadutyfree


import api.ApiDutyFree
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hvk.antalyadutyfree.adapters.MainListAdapter
import api.models.BrandModel

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="All Brand"

        //markaların gösterileceği listview
        val brandListView: ListView = findViewById(R.id.brand_list_view)

        //api üzerinden tüm markaların listesini al
        val brandList: ArrayList<BrandModel> = ArrayList()
        brandList.addAll(ApiDutyFree(this).getAllBrandList())

        val adapter = MainListAdapter(brandList, applicationContext)
        brandListView.adapter = adapter

        //marka listesi ana ekranı oluşturduğu için clicklistener listview'e eklenir
        brandListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            //tıklanan markanın ürünleri diğer activity'de gösterilir
            val brandModel: BrandModel = brandList[position]
            adapter.notifyDataSetChanged()

            //ürünlerin gösterileceği activity ve eklenen değerler
            val productIntent = Intent(this@MainActivity, ProductActivity::class.java)
            productIntent.putExtra("link", brandModel.link)
            productIntent.putExtra("name", brandModel.name)
            startActivity(productIntent)
        }
    }

    //çıkmak için geri tuşuna basılma durumunu saklar
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        //geri tuşuna 2.kez basılmış ise uygulamadan çıkılır
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        //geri tuşuna ilk kez basılmıştır
        //2 saniye içinde tekrar baılırsa uygulamadan çıkılır
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
}
