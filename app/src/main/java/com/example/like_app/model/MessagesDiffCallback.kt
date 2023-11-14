package com.example.like_app.model

import androidx.recyclerview.widget.DiffUtil

class MessagesDiffCallback(private val oldMessages: List<Message>, private val newMessages: List<Message>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldMessages.size
    }

    override fun getNewListSize(): Int {
        return newMessages.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMessage = oldMessages[oldItemPosition]
        val newMessage = newMessages[newItemPosition]
        return oldMessage.id == newMessage.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMessage = oldMessages[oldItemPosition]
        val newMessage = newMessages[newItemPosition]
        return oldMessage.mensaje == newMessage.mensaje &&
                oldMessage.nombre == newMessage.nombre &&
                oldMessage.type_mensaje == newMessage.type_mensaje &&
                oldMessage.hora == newMessage.hora &&
                oldMessage.urlFoto == newMessage.urlFoto
    }
}
