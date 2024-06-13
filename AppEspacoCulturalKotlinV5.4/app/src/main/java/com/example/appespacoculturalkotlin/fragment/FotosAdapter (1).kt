package com.example.appespacoculturalkotlin.fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.example.appespacoculturalkotlin.R
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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_foto, parent, false)
        }

        val imageView: ImageView = convertView!!.findViewById(R.id.foto)
        imageView.setImageResource(mFotos[position])

        return convertView
    }
}