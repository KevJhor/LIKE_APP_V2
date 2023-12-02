package com.example.like_app.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.like_app.R
import com.example.like_app.adapter.CartAdapter
import com.example.like_app.adapter.ItemMenuAdapter
import com.example.like_app.adapter.OrderAdapter
import com.example.like_app.databinding.FragmentHistorialRestBinding
import com.example.like_app.databinding.FragmentRestBinding
import com.example.like_app.model.DatosEmpresaModel
import com.example.like_app.model.ItemMenu
import com.example.like_app.model.OrderModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HistorialRestFragment : Fragment(),OrderAdapter.RecyclerViewEvent {
    private var _binding: FragmentHistorialRestBinding? = null
    private lateinit var navController: NavController
    private lateinit var currentUID: String
    private lateinit var orderUID: String
    private lateinit var brand_name: String

    private val binding get() = _binding!!
    var lstOrders: ArrayList<OrderModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentHistorialRestBinding.inflate(inflater, container, false)
        val view: View = binding.root
        var nombreCompleto=""
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val rvOrder: RecyclerView =view.findViewById(R.id.rvHistorialOrdenes)
        val userId=auth.currentUser!!.uid

        //OBTENER DATOS USUARIOS
        db.collection("users").document(userId)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        // El documento existe, puedes obtener los datos del cliente
                        val firstName = documentSnapshot.getString("firstName") ?: ""
                        val lastName = documentSnapshot.getString("lastName") ?: ""

                        // Concatenar nombre y apellido solo si ambos son no nulos
                        nombreCompleto ="$firstName $lastName"
                        // Utiliza los datos del cliente como desees
                        Log.i("InfoCliente", "Nombre Completo: $nombreCompleto")
                    } else {
                        // El documento no existe
                        Log.w("AdvertenciaCliente", "El documento del cliente no existe.")
                    }
                } else {
                    // Maneja el error de Firestore si es necesario
                    Log.e("ErrorCliente", "Error al obtener datos del cliente", task.exception)
                }
            }


        val brandName="KFC"

        Firebase.firestore.collection("orders")
            .whereEqualTo("brand_name", brandName)
            .whereEqualTo("estado", "Registrado")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        // Manejar cada documento que cumple con los criterios de la consulta
                        val orderId = document.id
                        val numOrdenID=formatearDocumentId(orderId)
                        val nomUser=nombreCompleto
                        val estado = document["estado"].toString()
                        val brand_name=document["brand_name"].toString()
                        val order= OrderModel(numOrdenID,brand_name,nomUser,estado)
                        lstOrders.add(order)
                        // Haz lo que necesites con los datos de cada documento

                    }
                    Log.i("InfoOrden", "ID de Orden: $lstOrders")
                    val adapter = OrderAdapter(lstOrders, this)
                    rvOrder.adapter= adapter
                    rvOrder.layoutManager= LinearLayoutManager(requireContext())

                } else {
                    // Manejar el error de la consulta si es necesario
                    Log.e("ErrorConsulta", "Error al realizar la consulta de órdenes: ${task.exception}")
                }
            }

        //SE LLENA EL RECYCLER VIEW DE MENUS







        return view

    }

    private fun formatearDocumentId(documentId: String): String {
        // Tomar los primeros 5 caracteres del documentId
        val primerosCincoCaracteres = documentId.take(5)

        // Agregar el prefijo "ord" a los 5 caracteres
        var idOrden= "ord$primerosCincoCaracteres"
        idOrden = idOrden.toUpperCase()
        return  idOrden
    }

    override fun onItemClick(position: Int) {

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}