package com.example.like_app.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.Message
import com.example.like_app.ui.adapter.MessageAdapter
import com.google.firebase.firestore.FirebaseFirestore


class ChatFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val messageCollection = db.collection("messages")
    private val messages = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val recyclerViewChat: RecyclerView = view.findViewById(R.id.recyclerViewChat)
        val buttonSend: Button = view.findViewById(R.id.buttonSend)
        val editTextMessage: EditText = view.findViewById(R.id.editTextMessage)

        // Inicializar RecyclerView y adaptador
        messageAdapter = MessageAdapter(messages)
        recyclerViewChat.adapter = messageAdapter
        recyclerViewChat.layoutManager = LinearLayoutManager(requireContext())

        // Cargar mensajes desde Firebase Firestore
        loadMessages()

        // Configurar el botón de envío de mensajes
        buttonSend.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            }
        }

        return view
    }

    private fun loadMessages() {
        messageCollection
            .orderBy("timestamp")  // Asegúrate de ordenar los mensajes por su marca de tiempo
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Manejar errores
                    return@addSnapshotListener
                }

                messages.clear()
                for (doc in snapshots!!) {
                    val messageText = doc.getString("text")
                    if (messageText != null) {
                        val message = Message(messageText)
                        messages.add(message)
                    }
                }
                messageAdapter.notifyDataSetChanged()
            }
    }


    private fun sendMessage(messageText: String) {
        val message = hashMapOf(
            "text" to messageText
        )

        messageCollection
            .add(message)
            .addOnSuccessListener { documentReference ->
                // El mensaje se envió con éxito, puedes realizar acciones adicionales aquí si es necesario.
            }
            .addOnFailureListener { e ->
                // Manejar errores al enviar el mensaje.
            }

        // Luego puedes agregar el mensaje a la lista local si lo deseas.
        val messageModel = Message(messageText)
        messages.add(messageModel)
        messageAdapter.notifyDataSetChanged()
    }


}

