package com.example.like_app.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.model.Message
import com.example.like_app.adapter.MessageAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.UUID


class ChatFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val messageCollection = db.collection("messages")
    private val messages = mutableListOf<Message>()
    private lateinit var messageAdapter: MessageAdapter
    private var usuario = ""
    private var nameU = ""
    private var lastU = ""
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
        val txtMensaje : EditText = view.findViewById(R.id.txtMensage);
        val btnEnviarFoto :ImageButton = view.findViewById(R.id.btnEnviarFoto);
        //val fotoPerfilCadena = "";
        val buttonSend: Button = view.findViewById(R.id.btnEnviar)

        // Inicializar RecyclerView y adaptador
        messageAdapter = MessageAdapter(messages)
        rvMensajes.adapter = messageAdapter
        rvMensajes.layoutManager = LinearLayoutManager(requireContext())

        // Cargar mensajes desde Firebase Firestore
        loadMessages()


        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val userID = user.uid

            val db = FirebaseFirestore.getInstance()
            val usersCollection = db.collection("users")



            usersCollection.document(userID)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        nameU = document.getString("firstName").toString()
                        lastU = document.getString("lastName").toString()
                        usuario = nameU + " " + lastU
                    } else {
                        // El documento no existe o está vacío
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar errores al obtener el nombre de usuario
                }


        }


        btnEnviarFoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/jpeg"
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"), PHOTO_SEND)

        }

        buttonSend.setOnClickListener{
            val messageText = txtMensaje.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val newMessage = Message(
                    mensaje = messageText,
                    nombre = usuario,
                    fotoPerfil = "URLFotoPerfilEjemplo",
                    type_mensaje = "1",
                    hora = Timestamp.now()
                )

                messageCollection
                    .add(newMessage)
                    .addOnSuccessListener { documentReference ->
                        // El mensaje se envió con éxito, puedes realizar acciones adicionales aquí si es necesario.

                        // Agregar el nuevo mensaje a la lista local y notificar al adaptador

                        messageAdapter.notifyDataSetChanged()
                        setScrollbar()
                        txtMensaje.setText("")
                    }
                    .addOnFailureListener { e ->
                        // Manejar errores al enviar el mensaje.
                        Log.e("ChatFragment", "Error al enviar el mensaje a Firebase", e)
                    }
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
        val photoUri = Uri.parse(messageText)
        storageReference = storage.getReference("imagenes_chat")
        val fotoReferencia: StorageReference? = photoUri?.let { storageReference?.child(it.lastPathSegment!!) }
        fotoReferencia?.putFile(photoUri)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // La foto se subió correctamente
                val downloadUrlTask = task.result?.storage?.downloadUrl

                downloadUrlTask?.addOnSuccessListener { downloadUrl ->
                    // Enviar el mensaje
                    val newMessage = Message(
                        mensaje = messageText,
                        nombre = "NombreEjemplo",  // Puedes cambiar esto según tus necesidades
                        fotoPerfil = "URLFotoPerfilEjemplo",  // Puedes cambiar esto según tus necesidades
                        type_mensaje = "2",
                        hora = Timestamp.now(),
                        urlFoto = downloadUrl.toString()
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
                }?.addOnFailureListener { e ->
                    // Manejar errores al obtener la URL de descarga.
                    Log.e("ChatFragment", "Error al obtener la URL de descarga", e)
                }
            } else {
                // Ocurrió un error al subir la foto
                Log.e("ChatFragment", "Error al subir la foto", task.exception)
            }
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHOTO_SEND && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            if (uri != null) {
                storageReference = storage.getReference("imagenes_chat")
                val photoId = UUID.randomUUID().toString()
                val fotoReferencia: StorageReference? =
                    uri?.let { storageReference?.child(photoId) }
                fotoReferencia?.putFile(uri)?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Utiliza task.result.storage.downloadUrl para obtener la URL de descarga
                        task.result?.storage?.downloadUrl?.addOnSuccessListener { downloadUrl ->
                            //Toast.makeText(requireContext(), "URL de descarga: $downloadUrl.toString()", Toast.LENGTH_SHORT).show()

                            val m = Message(
                                "Kevin te ha enviado una foto",
                                "NombreEjemplo",
                                downloadUrl.toString(),
                                "URLFotoPerfilEjemplo",
                                "2",
                                Timestamp.now()
                            )
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
        rvMensajes?.smoothScrollToPosition(messageAdapter.itemCount - 1)
    }





    private fun loadMessages() {

        val messageCollection = FirebaseFirestore.getInstance().collection("messages")
        val oldMessages = messages.toList()  // Guardar una copia de los mensajes actuales
        messageCollection
            .orderBy("hora", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    // Manejar errores
                    return@addSnapshotListener
                }

                messages.clear()
                val downloadUrlTasks = mutableListOf<Task<Uri>>()  // Para almacenar tareas de descarga de imágenes

                for (doc in snapshots!!) {
                    val mensaje = doc.getString("mensaje")
                    val nombre = doc.getString("nombre")
                    val fotoPerfil = doc.getString("fotoPerfil")
                    val typeMensaje = doc.getString("type_mensaje")
                    val timestamp = doc.getTimestamp("hora")
                    val foto = doc.getString("urlFoto") ?: ""

                    if (mensaje != null && nombre != null && fotoPerfil != null && typeMensaje != null && timestamp != null) {
                        val formattedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(timestamp.toDate())
                        val message = Message(mensaje, nombre,foto, fotoPerfil, typeMensaje, timestamp)
                        message.horaFormateada = formattedTime

                        // Cargar la imagen del almacenamiento de Firebase
                        val storageReference = storage.getReference("imagenes_chat/$fotoPerfil")
                        storageReference.getDownloadUrl().addOnSuccessListener { downloadUrl ->
                            message.urlFoto = downloadUrl.toString()
                            setScrollbar()

                        }.addOnFailureListener { e ->
                            Log.e("ChatFragment", "Error al cargar la imagen del almacenamiento de Firebase", e)
                        }

                        messages.add(message)
                    }
                }

                // Notificar al adaptador en el hilo principal
                activity?.runOnUiThread {
                    messageAdapter.notifyDataSetChanged()
                    setScrollbar()
                }
            }
    }



}
