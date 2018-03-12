package com.algorigo.algorigoutils

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*

/**
 * Created by jaehongyoo on 2018. 3. 9..
 */
class LocaleAdapter: BaseAdapter() {

    val array = Locale.getAvailableLocales()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var textView = convertView as? TextView?
        if (textView == null) {
            textView = TextView(parent?.context)
        }
        textView.setText(getItem(position).toString())
        return textView
    }

    override fun getItem(position: Int): Any {
        return array.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return array.size
    }
}