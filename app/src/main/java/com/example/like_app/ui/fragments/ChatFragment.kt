package com.example.like_app.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.Message
import com.example.like_app.adapter.MessageAdapter
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale


class ChatFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val messageCollection = db.collection("messages")
    private val messages = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter

    private val PHOTO_SEND = 1
    private val PHOTO_PERFIL = 2
    private lateinit var rvMensajes: RecyclerView

    private lateinit var storage: FirebaseStorage

    private lateinit var storageReference: StorageReference


    @SuppressLint("MissingInflatedId")
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        val fotoPerfil : CircleImageView = view.findViewById(R.id.fotoPerfil);

        storage = FirebaseStorage.getInstance()

        val nombre: TextView = view.findViewById(R.id.nombre);
        rvMensajes = view.findViewById(R.id.recyclerViewChat)
        val txtMensaje : EditText = view.findViewById(R.id.txtMensaje);
        val btnEnviarFoto :ImageButton = view.findViewById(R.id.btnEnviarFoto);
        val fotoPerfilCadena = "";
        val buttonSend: Button = view.findViewById(R.id.btnEnviar)

        // Inicializar RecyclerView y adaptador
        messageAdapter = MessageAdapter(messages)
        rvMensajes.adapter = messageAdapter
        rvMensajes.layoutManager = LinearLayoutManager(requireContext())

        // Cargar mensajes desde Firebase Firestore
        loadMessages()


        btnEnviarFoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"), PHOTO_SEND)
        }

        buttonSend.setOnClickListener{
            val messageText = txtMensaje.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)

                // Limpiar el campo de texto después de enviar el mensaje
                txtMensaje.text.clear()

            }
        }




        messageAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                setScrollbar()
            }
        })






        return view
    }



    private fun sendMessage(messageText: String) {
        val newMessage = Message(
            mensaje = messageText,
            nombre = "NombreEjemplo",  // Puedes cambiar esto según tus necesidades
            fotoPerfil = "URLFotoPerfilEjemplo",  // Puedes cambiar esto según tus necesidades
            type_mensaje = "tipoEjemplo",
            hora = Timestamp.now()
        )

        messageCollection
            .add(newMessage)
            .addOnSuccessListener { documentReference ->
                // El mensaje se envió con éxito, puedes realizar acciones adicionales aquí si es necesario.

                // Agregar el nuevo mensaje a la lista local y notificar al adaptador

                messageAdapter.notifyDataSetChanged()
                setScrollbar()
            }
            .addOnFailureListener { e ->
                // Manejar errores al enviar el mensaje.
                Log.e("ChatFragment", "Error al enviar el mensaje a Firebase", e)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            storageReference = storage.getReference("imagenes_chat")
            val fotoReferencia: StorageReference? = uri?.let { storageReference?.child(it.lastPathSegment!!) }
            fotoReferencia?.putFile(uri)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Utiliza task.result.storage.downloadUrl para obtener la URL de descarga
                    task.result?.storage?.downloadUrl?.addOnSuccessListener { downloadUrl ->
                        val m = Message("Kevin te ha enviado una foto", "NombreEjemplo", downloadUrl.toString(), "URLFotoPerfilEjemplo", "2", Timestamp.now())
                        messageCollection.add(m)
                        setScrollbar()
                    }?.addOnFailureListener { e ->
                        Log.e("ChatFragment", "Error al obtener la URL de descarga", e)
                    }
                } else {
                    Log.e("ChatFragment", "Error al subir la foto", task.exception)
                }
            }
        }

        /*else if (requestCode == PHOTO_PERFIL && resultCode == RESULT_OK) {
           val uri: Uri? = data?.data
           storageReference = storage.getReference("foto_perfil")
           val fotoReferencia: StorageReference? = uri?.let { storageReference?.child(it.lastPathSegment!!) }
           fotoReferencia?.putFile(uri).addOnSuccessListener(this) { taskSnapshot ->
               val downloadUrl: Uri? = taskSnapshot.downloadUrl
               fotoPerfilCadena = downloadUrl.toString()
               val m = MensajeEnviar("Kevin ha actualizado su foto de perfil", downloadUrl.toString(), nombre.text.toString(), fotoPerfilCadena, "2", Timestamp.now())
               databaseReference.push().setValue(m)
               Glide.with(this).load(downloadUrl.toString()).into(fotoPerfil)
           }
       }*/
    }


    private fun setScrollbar() {
        rvMensajes?.scrollToPosition(messageAdapter.itemCount - 1)
    }





    private fun loadMessages() {
        messageCollection
            .orderBy("hora", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Manejar errores
                    return@addSnapshotListener
                }

                messages.clear()
                for (doc in snapshots!!) {
                    val mensaje = doc.getString("mensaje")
                    val nombre = doc.getString("nombre")
                    val fotoPerfil = doc.getString("fotoPerfil")
                    val typeMensaje = doc.getString("type_mensaje")
                    val timestamp = doc.getTimestamp("hora")

                    if (mensaje != null && nombre != null && fotoPerfil != null && typeMensaje != null && timestamp != null) {
                        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp.toDate())
                        val message = Message(mensaje, nombre,"", fotoPerfil, typeMensaje, timestamp)
                        message.horaFormateada = formattedTime
                        messages.add(message)
                    }

                }
                // Notificar al adaptador después de esperar un breve periodo con Handler
                Handler().postDelayed({
                    messageAdapter.notifyDataSetChanged()
                }, 500) // Espera 500 milisegundos antes de notificar al adaptador
            }
    }


}
