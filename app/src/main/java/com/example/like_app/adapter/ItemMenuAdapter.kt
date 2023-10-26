package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.ItemMenu

class ItemMenuAdapter(private var lstItems:List<ItemMenu>)
    :RecyclerView.Adapter<ItemMenuAdapter.ViewHolder>(){
    /*LA CLASE VIEW HOLDER VA A TENER LOS ELEMENTOS QUE SE REPITEN*/
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val ivItem:ImageView= itemView.findViewById(R.id.ivItem)
        val tvTitle: TextView= itemView.findViewById(R.id.tvITitleItem)
        val tvInfo: TextView= itemView.findViewById(R.id.tvInfo)
        val tvPrice: TextView= itemView.findViewById(R.id.tvPrecioItem)
        val tvTime: TextView= itemView.findViewById(R.id.tvTime)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMenuAdapter.ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_menu,parent,false))
    }

    override fun onBindViewHolder(holder: ItemMenuAdapter.ViewHolder, position: Int) {
        val itemMenu= lstItems[position]
        holder.tvTitle.text=itemMenu.title
        holder.tvInfo.text=itemMenu.info
        holder.tvPrice.text=itemMenu.price
        holder.tvTime.text=itemMenu.time
        holder.ivItem.setImageResource(itemMenu.image)
    }

    override fun getItemCount(): Int {
        return lstItems.size

    }
}