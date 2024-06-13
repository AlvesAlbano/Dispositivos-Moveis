package com.example.appespacoculturalkotlin.adapter

import com.example.appespacoculturalkotlin.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class FotosAdapter(private val mContext: Context, private val mFotos: IntArray) : BaseAdapter() {

    override fun getCount(): Int {
        return mFotos.size
    }

    override fun getItem(position: Int): Any {
        return mFotos[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_foto, parent, false)
        }

        val imageView: ImageView = view!!.findViewById(R.id.foto)
        imageView.setImageResource(mFotos[position])

        return view
    }
}