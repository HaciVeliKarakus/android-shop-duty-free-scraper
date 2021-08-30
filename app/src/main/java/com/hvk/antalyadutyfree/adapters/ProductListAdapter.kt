package com.hvk.antalyadutyfree.adapters

import api.models.ProductModel
import android.app.AlertDialog
import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import com.hvk.antalyadutyfree.R
import com.squareup.picasso.Picasso


class ProductListAdapter(private val dataSet: ArrayList<*>, private val mContext: Context) :
    ArrayAdapter<Any>(mContext, R.layout.product_item, dataSet) {
    private class ViewHolder {
        // ürün adı gösterilir.
        lateinit var productName: TextView

        // ürününün güncel fiyatı gösterilir.
        lateinit var productPrice: TextView

        // ürünün indirimden önceki fiyatı gösterilir.
        lateinit var productPriceOld: TextView

        //ürününün resmi gösterilir
        lateinit var productImg: ImageView

        // ürünün detaylı açıklamarı gösterilir
        // içerik dinamik oluşturulur
        lateinit var tableLayout: TableLayout

        //detaylar ekranını açar
        lateinit var detailBtn: Button
    }

    override fun getCount(): Int {
        return dataSet.size
    }

    override fun getItem(position: Int): ProductModel {
        return dataSet[position] as ProductModel
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val viewHolder: ViewHolder
        val result: View
        if (cv == null) {
            viewHolder = ViewHolder()
            cv = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
            viewHolder.productName = cv.findViewById(R.id.prod_name)
            viewHolder.productPrice = cv.findViewById(R.id.prod_price)
            viewHolder.productPriceOld = cv.findViewById(R.id.prod_price_old)
            viewHolder.productImg = cv.findViewById(R.id.prod_img)
            viewHolder.tableLayout = cv.findViewById(R.id.table_layout)
            viewHolder.detailBtn = cv.findViewById(R.id.button)

            result = cv
            cv.tag = viewHolder

            //eklenecek olan ürün
            val item: ProductModel = getItem(position)

            // ürün fiyatları işlemleri
            viewHolder.productName.text = item.name
            if (item.price.contains(" ")) {
                viewHolder.productPrice.text = ("Only " + item.price.split(" ")[0])
                viewHolder.productPriceOld.text = item.price.split(" ")[1]
                viewHolder.productPriceOld.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                viewHolder.productPrice.text = ("Only " + item.price)
            }

            //detaylar butonu işlemleri
            viewHolder.detailBtn.transformationMethod = null
            viewHolder.detailBtn.setOnClickListener {
                val builder = AlertDialog.Builder(mContext).apply {
                    setTitle(item.name)
                    setMessage(item.detail)
                }
                val alertdialog: AlertDialog = builder.create()
                alertdialog.setCancelable(true)
                alertdialog.show()
            }

            try {
                // ürün resmi yükleme
                Picasso.get().load(item.imgSrc).fit().into(viewHolder.productImg)
            }
            catch (e:Exception){
                e.printStackTrace()
            }


            // ürünün bilgileri dinamik olarak oluşturulur
            for (i in item.infoList) {
                val row = TableRow(context)

                //sol kolon işlemleri
                val label = TextView(context)
                label.setTypeface(null, Typeface.BOLD)
                label.textSize = 15F
                label.textAlignment = View.TEXT_ALIGNMENT_TEXT_START
                label.text = i.label.substring(0, i.label.length.coerceAtMost(18))

                //sağ kolon işlemleri
                val data = TextView(context)
                data.textSize = 15F
                data.text = (":" + i.data.substring(0, i.data.length.coerceAtMost(18)))

                row.addView(label)
                row.addView(data)
                viewHolder.tableLayout.addView(row)
            }
        } else {
            cv.tag as ViewHolder
            result = cv
        }
        return result
    }
}
