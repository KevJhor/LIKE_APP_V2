package com.example.like_app.ui.payonline

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.like_app.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class DepositoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View= inflater.inflate(R.layout.fragment_deposito, container, false)

        val btnadd: Button = view.findViewById(R.id.btnConfirm)
        val db= FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val currentUID = auth.currentUser!!.uid
        btnadd.setOnClickListener {
            db.collection("orders")
                .whereEqualTo("uid", currentUID)
                .whereEqualTo("estado", "Activo")
                .get()
                .addOnSuccessListener {documents ->

                    for (document in documents) {
                        // Obtener el ID del documento y el campo "nombre" actual
                        val documentId = document.id



                        val newEstado = "Registrado"

                        // Actualizar el campo "nombre" en el documento específico
                        db.collection("orders")
                            .document(documentId)
                            .update("estado", newEstado)
                            .addOnSuccessListener {
                                // La actualización fue exitosa para este documento
                                Log.i("InfoRestFragment", "Actualización exitosa para el documento con ID: $documentId")
                            }
                            .addOnFailureListener { e ->
                                // Manejar el error para este documento
                                Log.e("ErrorRestFragment", "Error al actualizar el nombre del documento con ID: $documentId, Error: $e")
                            }





                    }
                }
                        .addOnFailureListener { e ->
                            Log.i(
                                "ErrorRestFragment",
                                "Error al actualizar el estado del documento: $e"
                            )
                        }
            findNavController().navigate(R.id.action_depositoFragment_to_estadoFragment)
        }

        return view
    }

}