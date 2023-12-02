package com.example.like_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.CartModel
import com.example.like_app.model.OrderModel
import com.squareup.picasso.Picasso
import java.util.Currency
import java.util.Locale

class OrderAdapter(  private var list:List<OrderModel>,private val listener: RecyclerViewEvent) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)  {
                val ivImgUser:ImageView=itemView.findViewById(R.id.ivUserOrder)
                val tvIdOrden:TextView=itemView.findViewById(R.id.tvIdOrden)
                val  tvNomUsuario:TextView=itemView.findViewById(R.id.tvNomUsuario)
                val   tvEstadoOrden:TextView=itemView.findViewById(R.id.tvEstado)

    }

    interface RecyclerViewEvent {
        fun onItemClick(position:Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return ViewHolder(inflateView)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemOrden= list[position]
        holder.tvIdOrden.text=itemOrden.idOrder
        holder.tvNomUsuario.text=itemOrden.nom_user
        holder.tvEstadoOrden.text=itemOrden.estado
        holder.ivImgUser.setImageResource(R.drawable.userorder)

    }


}