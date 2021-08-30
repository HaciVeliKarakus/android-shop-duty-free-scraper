package api

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import api.db.DBHelper
import api.models.BrandModel
import api.models.InformationModel
import api.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.util.*
import kotlin.collections.ArrayList

class ApiDutyFree(private var mContext: Context) {
    //veritabanı işlemlerine yardımcı class
    private var dbHelper = DBHelper(mContext)

    //bir marka için tüm ürünlerin bilgilerini tutar
    private var productList: ArrayList<ProductModel> = ArrayList()

    //tüm marka bilgilerini tutur
    private var brandList: ArrayList<BrandModel> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllProductList(mainLink: String) = runBlocking {
        // ilgili markaya ait tüm ürün bilgilerini gönderir
        val brandName = mainLink.split("/").last()
        // ilgili markanın ürünleri veritabanından getirir.
        // uygulama açıkken daha önce girildiyse sonuç listesi döner
        // ilk kez giriliyorsa siteden veriler toplanır
        // hem veritabanına kaydedilir hemde gönderilir.
        val resultList = dbHelper.readAllProduct(brandName)
        if (resultList.size > 0) {
            return@runBlocking resultList
        } else {
            Toast.makeText(mContext, "Loading...", Toast.LENGTH_SHORT).show()
            getProductWithJsoup(mainLink)
            for (prod in productList) {
                dbHelper.insertProduct(prod)
            }
        }
        return@runBlocking productList
    }

    fun getAllBrandList() = runBlocking {
        // tüm marka isimleri siteden çekilir
        // uygulama açık olduğu sürece sadece açılışta çalışır
        mContext.deleteDatabase(DBHelper.DATABASE_NAME)
        getBrandWithJsoup()
        for (brand in brandList) {
            dbHelper.insertBrand(brand)
        }
        return@runBlocking brandList
    }

    private suspend fun getProductWithJsoup(mainLink: String) = withContext(Dispatchers.IO) {
        // ilgili markaya ait tüm ürünlerin bilgileri siteden çekilir
        val brandName = mainLink.split("/").last()
        Jsoup.connect(mainLink)
            .get().run {
                getElementsByClass("product-item").forEach { data ->
                    val productDetailLink = data.getElementsByClass("product-item-link").attr("href")
//                    println("link $data")
                    Jsoup.connect(productDetailLink).get().run {
                        getElementsByClass("product-block-main-wrapper").forEach { _ ->

                            // ürünün tam adı
                            val name = getElementsByClass("base").text()

                            // ürünün fiyat bilgileri(güncel+indirim öncesi)
                            val price = select("span[class=price]").text()

                            // ürünün kaliteli görselinin linki
                            val imgSrc = select("meta[itemprop=image]").attr("content")

                            // uzun açıklamanın yer aldığı kısım
                            val detail = select("div[id=description]").text()

                            //detaylı bilgilerin yer aldığı kısım
                            val table =
                                select("table[id=product-attribute-specs-table]").select("tr")
                            val infoList: ArrayList<InformationModel> = ArrayList()
                            for (tw in table) {
                                val label = tw.select("th[class=col label]").text()
                                val data = tw.select("td[class=col data]").text()
                                infoList.add(InformationModel(label, data))
                            }
                            productList.run {
                                for (tw in table) {
                                    val label = tw.select("th[class=col label]").text()
                                    val data = tw.select("td[class=col data]").text()
                                    infoList.add(InformationModel(label, data))
                                }
                                add(
                                    ProductModel(
                                        brandName, name,
                                        price,
                                        imgSrc,
                                        detail,
                                        infoList
                                    )
                                )
                            }
                        }
                    }
                }
            }
    }

    private suspend fun getBrandWithJsoup() = withContext(Dispatchers.Default) {
        // tüm markaların isimleri ve ürünler sayfası linki siteden çekilir
        Jsoup.connect("https://antalya.shopdutyfree.com/en/brandboutique/brand/all/#brands-listing%20row")
            .get().run {
                select("div>a ").forEachIndexed { index, element ->
                    when (index) {
                        // marka isimlerinin bulunduğu aralık
                        in 7..115 -> {
                            val link = element.attr("href")
                            val name = element.text()
                            brandList.add(BrandModel(name, link))
                        }
                    }
                }
            }
    }
}