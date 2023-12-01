package com.example.like_app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.ComentarioModel
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query

class ComentariosAdapter(private val query: com.google.firebase.firestore.Query) : RecyclerView.Adapter<ComentariosAdapter.ComentarioViewHolder>() {

    private var listenerRegistration: ListenerRegistration? = null
    private val comentarios: MutableList<ComentarioModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item_2, parent, false)
        return ComentarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val comentario = comentarios[position]

        // Asignar datos a las vistas del ViewHolder
        holder.nombreMensaje.text = comentario.nombre
        holder.horaMensaje.text = comentario.hora
        holder.ratingBar.rating = comentario.calificacion
        holder.mensajeMensaje.text = comentario.mensaje
    }

    override fun getItemCount(): Int {
        return comentarios.size
    }

    inner class ComentarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreMensaje: TextView = itemView.findViewById(R.id.nombreMensaje)
        val horaMensaje: TextView = itemView.findViewById(R.id.horaMensaje)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar3)
        val mensajeMensaje: TextView = itemView.findViewById(R.id.mensajeMensaje)
    }

    fun startListening() {
        if (listenerRegistration == null) {
            listenerRegistration = query.orderBy("hora", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshots, exception ->
                if (exception != null) {
                    // Manejar error
                    return@addSnapshotListener
                }

                snapshots?.let { snapshot ->
                    comentarios.clear()
                    for (document in snapshot.documents) {
                        val nombre = document.getString("nombre")
                        val hora = document.getString("hora")
                        val calificacion = document.getDouble("calificacion")
                        val mensaje = document.getString("mensaje")

                        // Crea un ComentarioModel con los datos del documento
                        val comentario =
                            ComentarioModel(
                                nombre.toString(),
                                hora.toString(),
                                calificacion?.toFloat() ?: 0f,
                                mensaje.toString()
                            )
                        comentarios.add(comentario)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }
    fun stopListening() {
        listenerRegistration?.remove()
        listenerRegistration = null
    }
    fun cleanup() {
        stopListening()
    }
}

