package api.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import api.models.BrandModel
import api.models.InformationModel
import api.models.ProductModel


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        //uygulama açılınca ürünler ve markalar tablolarını oluşturur
        db.execSQL(
            "CREATE TABLE $PRODUCT_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$PRODUCT_BRAND_NAME TEXT,$NAME TEXT,$PRICE TEXT,$IMGSRC TEXT,$DETAIL TEXT,$INFO TEXT)"
        )
        db.execSQL(
            "CREATE TABLE $BRAND_TABLE ($BRAND_ID INTEGER PRIMARY KEY AUTOINCREMENT,$BRAND_NAME TEXT,$BRAND_URL TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $PRODUCT_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $BRAND_TABLE")
        onCreate(db)
    }

    fun insertProduct(product: ProductModel) {
        // bir adet ürünü veritabanına kaydeder
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(PRODUCT_BRAND_NAME, product.brandName)
        contentValues.put(NAME, product.name)
        contentValues.put(PRICE, product.price)
        contentValues.put(IMGSRC, product.imgSrc)
        contentValues.put(DETAIL, product.detail)
        contentValues.put(INFO, ArrayList2Text(product))
        db.insert(PRODUCT_TABLE, null, contentValues)
    }

    private fun ArrayList2Text(product: ProductModel): String {
        //ürünlerin detaylı bilgilerini veritabanına kaydedebilmek için tek bir text haline çevirir
        val sb = StringBuilder()
        product.infoList.forEach { item ->
            with(sb) { append(item.label + "|" + item.data + "|") }
        }
        return sb.toString()
    }

    fun insertBrand(brand: BrandModel) {
        //bir adet markayı veritabanına ekler
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(BRAND_NAME, brand.name)
        contentValues.put(BRAND_URL, brand.link)
        db.insert(BRAND_TABLE, null, contentValues)
    }

    fun readAllProduct(brandname: String): MutableList<ProductModel> {
        // ilgili markanın tüm ürünlerini gönderir
        val productList = mutableListOf<ProductModel>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $PRODUCT_TABLE WHERE $PRODUCT_BRAND_NAME=?"
        db.rawQuery(query, arrayOf(brandname)).use {
            if (it.moveToFirst()) {
                do {
                    val result = ProductModel()
                    result.name = it.getString(it.getColumnIndex(NAME))
                    result.price = it.getString(it.getColumnIndex(PRICE))
                    result.imgSrc = it.getString(it.getColumnIndex(IMGSRC))
                    result.detail = it.getString(it.getColumnIndex(DETAIL))
                    val txt = it.getString(it.getColumnIndex(INFO)).split("|")
                    result.infoList = text2ArrayList(txt)
                    productList.add(result)
                } while (it.moveToNext())
            }
        }
        return productList
    }

    private fun text2ArrayList(txt: List<String>): ArrayList<InformationModel> {
        // veritabanından gelen detaylı abilgiler text değerini kullanılacak olan
        // arraylisst yapısına çevirir
        val infoList: ArrayList<InformationModel> = ArrayList()
        val labelList: ArrayList<String> = ArrayList()
        val dataList: ArrayList<String> = ArrayList()
        txt.forEachIndexed { index, item ->
            if (txt.size - 1 == index)
                return@forEachIndexed

            if (index % 2 == 0) {
                labelList.add(item)
            } else {
                dataList.add(item)
            }
        }
        labelList.forEachIndexed { index, s ->
            val label = labelList[index]
            val data = dataList[index]

            infoList.add(
                InformationModel(
                    label.substring(0, label.length.coerceAtMost(18)),
                    data.substring(0, data.length.coerceAtMost(18))
                )
            )
        }
        return infoList
    }

    companion object {
        const val DATABASE_NAME = "product.db"

        const val BRAND_TABLE = "brand_table"
        const val BRAND_ID = "BRAND_ID"
        const val BRAND_NAME = "brand_name"
        const val BRAND_URL = "brand_url"

        const val PRODUCT_TABLE = "product_table"
        const val PRODUCT_BRAND_NAME = "PRODUCT_BRAND_NAME"
        const val ID = "ID"
        const val NAME = "NAME"
        const val PRICE = "PRICE"
        const val IMGSRC = "IMGSRC"
        const val DETAIL = "DETAIL"
        const val INFO = "INFO"
    }
}
