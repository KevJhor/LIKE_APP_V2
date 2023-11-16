package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.RestauranteModel
import com.squareup.picasso.Picasso

class RestauranteAdapter(private var lstRestaurantes:List<RestauranteModel>,private val listener:RecyclerViewEvent)
    : RecyclerView.Adapter<RestauranteAdapter.ViewHolder>(){
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener {

        val ivItemRest: ImageView = itemView.findViewById(R.id.ivItemRest)
        val tvNombreRest: TextView = itemView.findViewById(R.id.tvNomRest)
        val tvTiempoRest: TextView = itemView.findViewById(R.id.tvTiempoRest)

        init {
            itemView.setOnClickListener(this)

        }
        override fun onClick(p0: View?) {
            val position=adapterPosition
            if(position!=RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }

        }


    }


    fun actualizarLista(nuevaLista: List<RestauranteModel>) {
        lstRestaurantes = nuevaLista
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView:View=LayoutInflater.from(parent.context).inflate(R.layout.item_restaurante, parent, false)
        return ViewHolder(inflateView)
    }

    override fun getItemCount(): Int {
        return lstRestaurantes.size
    }

    override fun onBindViewHolder(holder: RestauranteAdapter.ViewHolder, position: Int) {
        val itemRest= lstRestaurantes[position]
        Picasso.get().load(itemRest.imageUrl).into(holder.ivItemRest)
        holder.tvNombreRest.text=itemRest.nombre
        holder.tvTiempoRest.text=itemRest.horario
    }

    interface RecyclerViewEvent {
        fun onItemClick(position:Int)
    }

}