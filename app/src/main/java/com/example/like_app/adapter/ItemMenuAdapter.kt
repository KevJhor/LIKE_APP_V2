package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.ItemMenu
import com.squareup.picasso.Picasso

class ItemMenuAdapter(private var lstItems:List<ItemMenu>,private val listener:RecyclerViewEvent)
    :RecyclerView.Adapter<ItemMenuAdapter.ViewHolder>(){
    /*LA CLASE VIEW HOLDER VA A TENER LOS ELEMENTOS QUE SE REPITEN*/
   inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView),View.OnClickListener{
        val ivItem:ImageView= itemView.findViewById(R.id.ivItem)
        val tvTitle: TextView= itemView.findViewById(R.id.tvITitleItem)
        val tvInfo: TextView= itemView.findViewById(R.id.tvInfo)
        val tvPrice: TextView= itemView.findViewById(R.id.tvPrecioItem)

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



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemMenuAdapter.ViewHolder {
        val layoutInflater=LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_menu,parent,false))
    }

    override fun onBindViewHolder(holder: ItemMenuAdapter.ViewHolder, position: Int) {
        val itemMenu= lstItems[position]
        holder.tvTitle.text=itemMenu.title
        holder.tvInfo.text=itemMenu.info
        holder.tvPrice.text=itemMenu.price
        Picasso.get().load(itemMenu.imageUrl).into(holder.ivItem)
    }

    override fun getItemCount(): Int {
        return lstItems.size

    }

    interface RecyclerViewEvent {
        fun onItemClick(position:Int)
    }
}