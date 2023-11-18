package com.example.like_app.ui.fragments

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.ComentariosAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class ComentariosFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var comentariosAdapter: ComentariosAdapter
    private lateinit var firestoreDB: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_comentarios, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewC)
        firestoreDB = FirebaseFirestore.getInstance()
        var comentario: EditText = view.findViewById(R.id.txtMensag)
        val btnEnviar: Button = view.findViewById(R.id.btnEnvi)

        val califi: RatingBar = view.findViewById(R.id.ratingBar2)



        btnEnviar.setOnClickListener {
            EnviarComentario(comentario.text.toString(),califi)
            comentario.setText("")
        }
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun EnviarComentario(txt: String, estrella: RatingBar){
        val hora = LocalDateTime.now()
        val hora_24 = hora.format(DateTimeFormatter.ofPattern("HH:mm a"))
        val comentario = hashMapOf(
            "nombre" to "Lazaro Fuller",
            "hora" to hora_24.toString(),
            "calificacion" to estrella.rating,
            "mensaje" to txt
        )

        if (txt.isNotEmpty() && estrella.numStars in 0..5) {
            firestoreDB.collection("comentarios")
                .add(comentario)
                .addOnSuccessListener { documentReference ->
                    // La colección se ha creado exitosamente
                }
                .addOnFailureListener { e ->
                    // Error al crear la colección
                }
        } else {
            // Mostrar un mensaje de error
        }
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