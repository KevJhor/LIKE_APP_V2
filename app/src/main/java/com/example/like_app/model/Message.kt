package com.example.like_app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
data class Message(
    val mensaje: String,
    val nombre: String,
    val urlFoto: String = "",
    val fotoPerfil: String,
    val type_mensaje: String,
    val hora: Timestamp = Timestamp.now(),  // Campo de marca de tiempo,
    var horaFormateada: String = ""  // Campo adicional para almacenar la hora formateada



){

}