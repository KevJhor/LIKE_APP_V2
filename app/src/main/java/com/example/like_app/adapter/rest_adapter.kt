package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.model_estab
import com.example.like_app.model.model_rest

class rest_adapter(private val lstEstab: List<model_rest>) : RecyclerView.Adapter<rest_adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: View = itemView.findViewById(R.id.viewImgRest)
        val name: TextView = itemView.findViewById(R.id.tvTitleRest)
        val descripcion: TextView = itemView.findViewById(R.id.tvInfoRest)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycler_view_item_1, parent, false))
    }

    override fun getItemCount(): Int {
        return lstEstab.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemEstab = lstEstab[position]
        val viewImgRest = holder.image
        viewImgRest.setBackgroundResource(itemEstab.image)
        holder.name.text = itemEstab.name
        holder.descripcion.text = itemEstab.descr


    }

}