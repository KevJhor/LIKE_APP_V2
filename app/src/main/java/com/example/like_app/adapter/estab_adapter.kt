package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.model_estab

class estab_adapter(private val lstEstab: List<model_estab>) : RecyclerView.Adapter<estab_adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: View = itemView.findViewById(R.id.viewImgRest)
        val name: TextView = itemView.findViewById(R.id.tvTitleRest)
        val descr: TextView = itemView.findViewById(R.id.tvInfoRest)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemEstab = lstEstab[position]
        holder.image.setBackgroundResource(itemEstab.image)
        holder.name.text = itemEstab.name
        holder.descr.text = itemEstab.descr
    }

    override fun getItemCount(): Int {
        return lstEstab.size
    }
}
