package com.example.appespacoculturalkotlin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class RecentesAdapter(private var itemList: List<RecentesData>) : RecyclerView.Adapter<RecentesAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.item_title)
        val subtitleTextView: TextView = itemView.findViewById(R.id.item_subtitle)
        val imagemView: ImageView = itemView.findViewById(R.id.item_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recente_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.titleTextView.text = item.Nome
        holder.subtitleTextView.text = item.Autor
        // Carregar a imagem usando Glide
        Glide.with(holder.itemView.context)
            .load(item.Imagem)
            .into(holder.imagemView)
    }

    override fun getItemCount() = itemList.size

    fun updateData(newItemList: List<RecentesData>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}