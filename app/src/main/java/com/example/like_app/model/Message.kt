package com.example.like_app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

data class Message(
    val mensaje: String,
    val nombre: String,
    var urlFoto: String = "",
    val fotoPerfil: String,
    val type_mensaje: String,
    val hora: Timestamp = Timestamp.now(),  // Campo de marca de tiempo,
    var horaFormateada: String = "",
    val id: String = UUID.randomUUID().toString()  // Campo de id



){

}