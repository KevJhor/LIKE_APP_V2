package com.example.like_app.adapter

import com.google.firebase.database.FirebaseDatabase

class PagoAdapter {
    private val database = FirebaseDatabase.getInstance()
    private val ordenesRef = database.getReference("orders")

    fun confirmarPago(idOrden: String) {
        // Actualizar el campo "pagada" de la orden a true
        ordenesRef.child(idOrden).child("estado").setValue("pagado")
            .addOnSuccessListener {
                // La confirmación de pago fue exitosa
                println("Pago confirmado para la orden $idOrden.")
            }
            .addOnFailureListener { error ->
                // Hubo un error al confirmar el pago
                println("Error al confirmar el pago: $error")
            }
    }
}
