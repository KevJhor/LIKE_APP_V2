package com.example.like_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ComentariosAdapter
import com.example.like_app.model.ComentarioModel
import com.google.firebase.firestore.FirebaseFirestore


class ComentariosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var comentariosAdapter: ComentariosAdapter
    private lateinit var firestoreDB: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_comentarios, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewC)
        firestoreDB = FirebaseFirestore.getInstance()
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comentariosAdapter = ComentariosAdapter(firestoreDB.collection("comentarios"))
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = comentariosAdapter
        }
        comentariosAdapter.startListening()
       /* val comentario = hashMapOf(
            "nombre" to "Usuario Ejemplo",
            "hora" to "12:00",
            "calificacion" to 5.0,
            "mensaje" to "¡Hola, este es un comentario de prueba!"
        )

        firestoreDB.collection("comentarios")
            .add(comentario)
            .addOnSuccessListener { documentReference ->
                // La colección se ha creado exitosamente
            }
            .addOnFailureListener { e ->
                // Error al crear la colección
            }*/

    }

    override fun onDestroyView() {
        super.onDestroyView()
        comentariosAdapter.cleanup()
    }

    /*private fun generarDatosDeComentarios(): List<ComentarioModel> {
        // Aquí debes proporcionar tus datos reales de comentarios
        // Por ejemplo:
        val comentarios = mutableListOf<ComentarioModel>()
        comentarios.add(ComentarioModel("Julius Arteaga", "10:00", 4.0f, "Este es un comentario de ejemplo 1"))
        comentarios.add(ComentarioModel("Jean Martinez", "11:00", 3.5f, "Este es un comentario de ejemplo 2"))
        // Agregar más datos de comentarios según sea necesario
        return comentarios
    }*/

}