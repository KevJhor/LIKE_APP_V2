package com.example.like_app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.ui.model.model_estab

class estab_adapter(private val lstEstab: List<model_estab>) : RecyclerView.Adapter<estab_adapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.imageEstab)
        val estabName: TextView = itemView.findViewById(R.id.text1Estab)
        val category: TextView = itemView.findViewById(R.id.text2Estab)
        val tiempo: TextView = itemView.findViewById(R.id.text3Estab)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.recycler_view_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemEstab = lstEstab[position]
        holder.image.setImageResource(itemEstab.image)
        holder.estabName.text = itemEstab.estabName
        holder.category.text = itemEstab.category
        holder.tiempo.text = itemEstab.tiempo + "- Envio: S/."+ itemEstab.precioEnvio.toString()

    }

    override fun getItemCount(): Int {
        return lstEstab.size
    }
}
