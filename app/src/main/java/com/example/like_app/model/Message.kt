package com.example.like_app.model

data class Message(
    val messageId: String, // Un identificador único para el mensaje
    val senderId: String,  // El ID del remitente
    val text: String,      // El contenido del mensaje
    val timestamp: Long    // Una marca de tiempo para el mensaje
){
    constructor(text: String) : this("", "", text, 0)
}