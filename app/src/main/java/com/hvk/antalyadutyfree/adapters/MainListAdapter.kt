package com.hvk.antalyadutyfree.adapters

import api.models.BrandModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.hvk.antalyadutyfree.R

class MainListAdapter(private val dataset: ArrayList<*>, mContext: Context) :
    ArrayAdapter<Any>(mContext, R.layout.brand_item, dataset) {
    private class ViewHolder {
        // marka adı gösterilir
        lateinit var brandName: TextView
        // marka adının baş harfi gösterilir
        lateinit var brandLetter: TextView
    }

    override fun getCount(): Int {
        return dataset.size
    }

    override fun getItem(position: Int): BrandModel {
        return dataset[position] as BrandModel
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val viewHolder: ViewHolder
        val result: View
        if (cv == null) {
            viewHolder = ViewHolder()
            cv = LayoutInflater.from(parent.context).inflate(R.layout.brand_item, parent, false)
            viewHolder.brandName = cv.findViewById(R.id.brand_name_tv)
            viewHolder.brandLetter = cv.findViewById(R.id.brand_letter_tv)

            result = cv
            cv.tag = viewHolder
        } else {
            viewHolder = cv.tag as ViewHolder
            result = cv
        }
        //eklenecek olan marka
        val brand: BrandModel = getItem(position)
        viewHolder.brandName.text = brand.name
        viewHolder.brandLetter.text = brand.name[0].toString()

        return result
    }
}