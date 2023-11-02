package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.RestauranteModel
import com.squareup.picasso.Picasso

class RestauranteAdapter(private var lstRestaurantes:List<RestauranteModel>)
    : RecyclerView.Adapter<RestauranteAdapter.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val ivItemRest: ImageView = itemView.findViewById(R.id.ivItemRest)
        val tvNombreRest: TextView = itemView.findViewById(R.id.tvNomRest)
        val tvTiempoRest: TextView = itemView.findViewById(R.id.tvTiempoRest)
        val tvPrecioEnvio: TextView = itemView.findViewById(R.id.tvPrecioEnvioRest)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_restaurante, parent, false))
    }

    override fun getItemCount(): Int {
        return lstRestaurantes.size
    }

    override fun onBindViewHolder(holder: RestauranteAdapter.ViewHolder, position: Int) {
        val itemRest= lstRestaurantes[position]
        Picasso.get().load(itemRest.imageUrl).into(holder.ivItemRest)
        holder.tvNombreRest.text=itemRest.nombre
        holder.tvTiempoRest.text=itemRest.tiempo
        holder.tvPrecioEnvio.text=itemRest.precioEnvio


    }

}