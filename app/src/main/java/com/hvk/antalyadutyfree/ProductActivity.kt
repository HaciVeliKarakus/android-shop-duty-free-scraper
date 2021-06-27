package com.hvk.antalyadutyfree

import api.ApiDutyFree
import api.models.ProductModel
import android.os.Build
import android.os.Bundle
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.hvk.antalyadutyfree.adapters.ProductListAdapter

@Suppress("DEPRECATION")

class ProductActivity : AppCompatActivity() {
    // ürünlerin bulunduğu sayfanın linki
    lateinit var mainLink: String

    // veritabanı işlemleri için gerekli marka adı
    lateinit var mainName: String

    //android 8.0 ve üzeri gereklidir
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        // mainactivity den gelen değerleri kullanılabilir hale getir
        mainLink = intent.getStringExtra("link").toString()
        mainName = intent.getStringExtra("name").toString()

        // başlık mevcut marka adı yapılır
        title = mainName

        // ürünlerin gösterileceği temel listview
        val listView: ListView = findViewById(R.id.product_list_view)

        // api üzerinde tüm ürünlerle ilgili bilgiler alınır
        val productModelList: ArrayList<ProductModel> = ArrayList()
        productModelList.addAll(ApiDutyFree(this).getAllProductList(mainLink))

        val adapter = ProductListAdapter(productModelList, this)
        listView.adapter = adapter
        // ürünler kısmında click eventi sadece detaylar butonunda tanımlanacağı için
        // adapter içerisinde yer alır.
    }
}
