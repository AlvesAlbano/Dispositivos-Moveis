package com.example.appespacoculturalkotlin.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appespacoculturalkotlin.Adm
import com.example.appespacoculturalkotlin.DeviceScanActivity
import com.example.appespacoculturalkotlin.ObraActivity
import com.example.appespacoculturalkotlin.R
import com.example.appespacoculturalkotlin.telas_adm.ObraEditarAdmActivity

class AdapterObras(private val uuidList: List<String>, private val listener: OnItemClickListener) : RecyclerView.Adapter<AdapterObras.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textViewUuid: TextView = itemView.findViewById(R.id.textViewObra)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val uuid = uuidList[position]
                listener.onItemClick(uuid)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_obra, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uuid = uuidList[position]
        holder.textViewUuid.text = uuid

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            val intent = if (Adm.admValor) {
                Intent(context, ObraEditarAdmActivity::class.java).apply {
                    putExtra("uuid", DeviceScanActivity.nomeBeacon)
                }
            } else {
                Intent(context, ObraActivity::class.java).apply {
                    putExtra("uuid", DeviceScanActivity.nomeBeacon)
                }
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return uuidList.size
    }

    interface OnItemClickListener {
        fun onItemClick(uuid: String)
    }
}