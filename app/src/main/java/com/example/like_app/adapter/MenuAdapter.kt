package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.MenuModel


class MenuAdapter(private var lstItems:List<MenuModel>)
    : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val tvNumMenu:TextView=itemView.findViewById(R.id.tvNumMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.ViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.itembtnmenu, parent, false))
    }

    override fun onBindViewHolder(holder: MenuAdapter.ViewHolder, position: Int) {
        val item= lstItems[position]
        holder.tvNumMenu.text=item.etiqueta
    }

    override fun getItemCount(): Int {
        return lstItems.size
    }
}