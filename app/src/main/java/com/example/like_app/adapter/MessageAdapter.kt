package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.like_app.R
import com.example.like_app.model.Message
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale

class MessageAdapter(private val messages: List<Message>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre :TextView = itemView.findViewById(R.id.nombreMensaje)
        val mensaje :TextView = itemView.findViewById(R.id.mensajeMensaje)
        val hora : (TextView) = itemView.findViewById(R.id.horaMensaje)
        val fotoMensajePerfil : (CircleImageView) = itemView.findViewById(R.id.fotoPerfilMensaje)
        val fotoMensaje : (ImageView) = itemView.findViewById(R.id.mensajeFoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.nombre.text = message.nombre
        holder.mensaje.text = message.mensaje
        // Verificar si el campo hora no es nulo antes de establecer el texto
        if (message.hora != null) {
            // Formatear la hora y establecer el texto
            val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(message.hora.toDate())
            holder.hora.text = formattedTime
        } else {
            // Puedes establecer un valor predeterminado o dejarlo vacío según tus necesidades
            holder.hora.text = "Hora no disponible"
        }
         if (message.type_mensaje.equals("2")) {
             holder.fotoMensaje.visibility = View.VISIBLE
             holder.mensaje.visibility = View.VISIBLE
             Glide.with(holder.itemView.context)
                 .load(message.urlFoto)
                 .into(holder.fotoMensaje)
         }
         else if (message.type_mensaje.equals("1")) {
             holder.fotoMensaje.visibility = View.GONE
             holder.mensaje.visibility = View.VISIBLE
         }



    }

    override fun getItemCount(): Int {
        return messages.size
    }
}